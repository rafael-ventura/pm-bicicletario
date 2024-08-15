package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AluguelService {

    private static final Logger log = LoggerFactory.getLogger(AluguelService.class);

    private final AluguelRepository aluguelRepository;
    private final BicicletaService bicicletaService;
    private final TrancaService trancaService;
    private final AdministradoraCCService administradoraCCService;
    private final EmailService emailService;

    public AluguelService(AluguelRepository aluguelRepository,
                          BicicletaService bicicletaService,
                          TrancaService trancaService,
                          AdministradoraCCService administradoraCCService,
                          EmailService emailService) {
        this.aluguelRepository = aluguelRepository;
        this.bicicletaService = bicicletaService;
        this.trancaService = trancaService;
        this.administradoraCCService = administradoraCCService;
        this.emailService = emailService;
    }

    public Aluguel alugarBicicleta(int idCiclista, int idTranca) {
        // Verifica se o ciclista já possui um aluguel ativo
        if (aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)) {
            log.warn("Ciclista já possui um aluguel ativo. ID Ciclista: {}", idCiclista);
            throw new InvalidDataException("Ciclista já possui um aluguel ativo.");
        }

        // Valida a tranca
        NovoTrancaDTO tranca = trancaService.obterTranca(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException("Tranca não encontrada."));
        if (!StatusTranca.OCUPADA.equals(tranca.getStatus())) {
            log.warn("Tranca com status inválido. ID Tranca: {}", idTranca);
            throw new InvalidDataException("Tranca não está ocupada.");
        }

        // Lê a bicicleta presa na tranca
        Bicicleta bicicleta = bicicletaService.getBicicleta(tranca.getBicicleta())
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta não encontrada."));
        if (!StatusBicicleta.DISPONIVEL.equals(bicicleta.getStatusBicicleta())) {
            log.warn("Bicicleta com status inválido. ID Bicicleta: {}", bicicleta.getId());
            throw new BadRequestException("Bicicleta não está disponível.");
        }

        // Verifica se a bicicleta não está em reparo
        if (StatusBicicleta.EM_REPARO.equals(bicicleta.getStatusBicicleta())) {
            log.warn("Bicicleta em reparo. ID Bicicleta: {}", bicicleta.getId());
            throw new BadRequestException("Bicicleta não pode ser alugada.");
        }

        // Envia a cobrança para a Administradora CC
        boolean pagamentoAutorizado = administradoraCCService.processarPagamento(idCiclista, 10.00);
        if (!pagamentoAutorizado) {
            log.error("Pagamento não autorizado para ciclista: {}", idCiclista);
            throw new BadRequestException("Pagamento não autorizado.");
        }

        // Registra os dados da retirada da bicicleta
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(idCiclista);
        aluguel.setBicicleta(bicicleta.getId());
        aluguel.setTrancaInicio(idTranca);
        aluguel.setHoraInicio(String.valueOf(LocalDateTime.now()));
        aluguelRepository.save(aluguel);

        // Altera o status da bicicleta para "em uso"
        bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.EM_USO);

        // Solicita abertura da tranca e altera status para "livre"
        trancaService.atualizarStatusTranca(idTranca, StatusTranca.LIVRE);

        // Envia uma mensagem para o ciclista com os dados do aluguel
        emailService.enviarEmailAluguel(idCiclista, aluguel);

        log.info("Aluguel realizado com sucesso para ciclista: {}", idCiclista);
        return aluguel;
    }
}
