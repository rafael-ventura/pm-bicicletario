package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.*;
import com.example.bicicletario.bicicletario.domain.*;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.DevolucaoRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DevolucaoService {

    private final DevolucaoRepository devolucaoRepository;
    private final AluguelRepository aluguelRepository;
    private final BicicletaService bicicletaService;
    private final TrancaService trancaService;
    private final AdministradoraCCService administradoraCCService;
    private final EmailService emailService;

    public DevolucaoService(DevolucaoRepository devolucaoRepository,
                            AluguelRepository aluguelRepository,
                            BicicletaService bicicletaService,
                            TrancaService trancaService,
                            AdministradoraCCService administradoraCCService,
                            EmailService emailService) {
        this.devolucaoRepository = devolucaoRepository;
        this.aluguelRepository = aluguelRepository;
        this.bicicletaService = bicicletaService;
        this.trancaService = trancaService;
        this.administradoraCCService = administradoraCCService;
        this.emailService = emailService;
    }

    public Devolucao realizarDevolucao(NovoDevolucaoDTO devolucaoDTO) {
        int idBicicleta = devolucaoDTO.getIdBicicleta();
        int idTranca = devolucaoDTO.getIdTranca();

        // 1. Validação da bicicleta
        Bicicleta bicicleta = bicicletaService.getBicicleta(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta não encontrada."));

        if (!StatusBicicleta.EM_USO.equals(bicicleta.getStatusBicicleta())) {
            throw new InvalidDataException("Bicicleta não está em uso.");
        }

        // 2. Validação da tranca
        NovoTrancaDTO tranca = trancaService.obterTranca(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException("Tranca não encontrada."));

        if (!"livre".equalsIgnoreCase(tranca.getStatus())) {
            throw new InvalidDataException("Tranca não está disponível para devolução.");
        }

        // 3. Busca do aluguel ativo
        Optional<Aluguel> aluguelOptional = aluguelRepository.findByBicicletaAndHoraFimIsNull(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException("Aluguel ativo não encontrado para esta bicicleta."));

        if (aluguelOptional.isEmpty()) {
            throw new ResourceNotFoundException("Aluguel ativo não encontrado para esta bicicleta.");
        }

        Aluguel aluguel = aluguelOptional.get();

        LocalDateTime horaInicio = LocalDateTime.parse(aluguel.getHoraInicio());
        LocalDateTime horaFim = LocalDateTime.now();

        // 4. Cálculo do valor extra
        Duration duracao = Duration.between(horaInicio, horaFim);
        long minutosExcedentes = duracao.toMinutes() - 120; // 2 horas = 120 minutos
        double valorExtra = 0.0;

        if (minutosExcedentes > 0) {
            long unidadesDe30Min = (long) Math.ceil(minutosExcedentes / 30.0);
            valorExtra = unidadesDe30Min * 5.0; // R$5,00 por cada 30 minutos excedentes
        }

        // 5. Processamento de pagamento extra, se aplicável
        boolean pagamentoRealizado = true;
        if (valorExtra > 0) {
            pagamentoRealizado = administradoraCCService.processarPagamento(aluguel.getCiclista(), valorExtra);
        }

        // 6. Atualização do aluguel
        aluguel.setHoraFim(horaFim.toString());
        aluguelRepository.save(aluguel);

        // 7. Atualização da bicicleta
        bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.DISPONIVEL);

        // 8. Atualização da tranca
        trancaService.atualizarStatusTranca(idTranca, "ocupada");
        trancaService.prenderBicicleta(idTranca, idBicicleta);

        // 9. Registro da devolução
        Devolucao devolucao = new Devolucao();
        devolucao.setIdAluguel(aluguel.getId());
        devolucao.setIdBicicleta(idBicicleta);
        devolucao.setIdTranca(idTranca);
        devolucao.setDataHoraDevolucao(String.valueOf(horaFim));
        devolucao.setValorExtra(valorExtra);
        devolucao.setCartaoUsado("Dados do cartão do ciclista"); // Este dado precisa ser obtido de alguma forma
        devolucao.setStatusPagamento(pagamentoRealizado ? "SUCESSO" : "FALHA");
        if (valorExtra > 0) {
            devolucao.setDataHoraCobranca(String.valueOf(horaFim));
        }
        devolucaoRepository.save(devolucao);

        // 10. Envio de e-mail ao ciclista
        emailService.enviarEmailDevolucao(aluguel.getCiclista(), devolucao);

        return devolucao;
    }
}
