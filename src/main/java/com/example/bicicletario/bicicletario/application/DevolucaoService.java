package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.*;
import com.example.bicicletario.bicicletario.domain.*;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.DevolucaoRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class DevolucaoService {

    private final DevolucaoRepository devolucaoRepository;
    private final AluguelRepository aluguelRepository;
    private final BicicletaService bicicletaService;
    private final TrancaService trancaService;
    private final AdministradoraCCService administradoraCCService;
    private final EmailService emailService;
    private final CiclistaService ciclistaService;

    public DevolucaoService(DevolucaoRepository devolucaoRepository,
                            AluguelRepository aluguelRepository,
                            BicicletaService bicicletaService,
                            TrancaService trancaService,
                            AdministradoraCCService administradoraCCService,
                            EmailService emailService, CiclistaService ciclistaService) {
        this.devolucaoRepository = devolucaoRepository;
        this.aluguelRepository = aluguelRepository;
        this.bicicletaService = bicicletaService;
        this.trancaService = trancaService;
        this.administradoraCCService = administradoraCCService;
        this.emailService = emailService;
        this.ciclistaService = ciclistaService;
    }

    public static final int TEMPO_LIMITE_GRATIS = 120; // 2 horas em minutos
    public static final double TAXA_POR_MEIA_HORA = 5.00; // R$5,00 para cada meia hora extra
    public static final String STATUS_SUCESSO = "SUCESSO";
    public static final String STATUS_FALHA = "FALHA";

    public Devolucao realizarDevolucao(NovoDevolucaoDTO devolucaoDTO) {
        int trancaFim = devolucaoDTO.getIdTranca();
        int bicicletaId = devolucaoDTO.getIdBicicleta();

        // 1. Validação da bicicleta
        Bicicleta bicicleta = bicicletaService.getBicicletaById(bicicletaId);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO); // Simula a bicicleta sendo usada
        if (!StatusBicicleta.EM_USO.equals(bicicleta.getStatusBicicleta())) {
            throw new InvalidDataException("Bicicleta não está em uso.");
        }

        // 2. Validação da tranca
        Tranca tranca = trancaService.obterTranca(trancaFim);

        tranca.setStatus(StatusTranca.LIVRE); // Simula a tranca livre
        if (!StatusTranca.LIVRE.equals(tranca.getStatus())) {
            throw new InvalidDataException("Tranca não está disponível para devolução.");
        }

        // 3. Busca do aluguel ativo
        Aluguel aluguel = aluguelRepository.findByBicicletaAndHoraFimIsNull(Integer.parseInt(bicicleta.getId().toString()))
                .orElseThrow(() -> new ResourceNotFoundException("Aluguel ativo não encontrado para esta bicicleta."));

        LocalDateTime horaInicio = LocalDateTime.parse(aluguel.getHoraInicio());
        LocalDateTime horaFim = LocalDateTime.now();

        // 4. Cálculo do valor extra
        Duration duracao = Duration.between(horaInicio, horaFim);
        long minutosExcedentes = duracao.toMinutes() - TEMPO_LIMITE_GRATIS; // Excede o limite de 2 horas
        double valorExtra = 0.0;

        if (minutosExcedentes > 0) {
            long unidadesDe30Min = (long) Math.ceil(minutosExcedentes / 30.0);
            valorExtra = unidadesDe30Min * TAXA_POR_MEIA_HORA;
        }

        // 5. Processamento de pagamento extra, se aplicável
        NovoCobrancaDTO cobranca = new NovoCobrancaDTO();
        boolean pagamentoRealizado = true;
        if (valorExtra > 0) {
            pagamentoRealizado = administradoraCCService.enviarCobranca(cobranca);
        }

        // 6. Atualização do aluguel
        aluguel.setHoraFim(horaFim.toString());
        aluguelRepository.save(aluguel);

        // 7. Atualização da bicicleta
        bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.DISPONIVEL);

        // 8. Atualização da tranca
        trancaService.trancarTranca(trancaFim, bicicleta.getId());
        trancaService.prenderBicicleta(Integer.parseInt(bicicleta.getId().toString()), trancaFim);

        // 9. Registro da devolução
        Devolucao devolucao = new Devolucao();
        devolucao.setIdAluguel(aluguel.getId());
        devolucao.setIdBicicleta(Integer.parseInt(bicicleta.getId().toString()));
        devolucao.setIdTranca(trancaFim);
        devolucao.setDataHoraDevolucao(horaFim.toString());
        devolucao.setValorExtra(valorExtra);
        devolucao.setCartaoUsado("Dados do cartão do ciclista"); // Este dado precisa ser obtido do serviço de pagamento
        devolucao.setStatusPagamento(pagamentoRealizado ? STATUS_SUCESSO : STATUS_FALHA);

        if (valorExtra > 0) {
            devolucao.setDataHoraCobranca(horaFim.toString());
        }

        devolucaoRepository.save(devolucao);

        // 10. Envio de e-mail ao ciclista
        String email = ciclistaService.obterCiclista(aluguel.getCiclista()).map(Ciclista::getEmail).orElse("");
        emailService.enviarEmailDevolucao(email, aluguel, bicicleta, tranca, valorExtra, devolucao.getCartaoUsado(), devolucao.getStatusPagamento(), devolucao.getDataHoraCobranca());

        return devolucao;
    }
}
