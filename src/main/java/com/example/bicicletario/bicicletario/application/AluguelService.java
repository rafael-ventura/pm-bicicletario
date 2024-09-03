package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
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
import java.util.Optional;

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

    public Aluguel aluguel(int idCiclista, int idTranca) {
        // Verifica se o ciclista já possui um aluguel ativo (E1)
        if (aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)) {
            log.warn("Ciclista já possui um aluguel ativo. ID Ciclista: {}", idCiclista);
            // Envia email para o ciclista com os dados do aluguel atual
            Optional<Aluguel> aluguelAtual = aluguelRepository.findByCiclistaAndHoraFimIsNull(idCiclista);
            emailService.enviarEmailAluguelExistente(idCiclista, aluguelAtual.get());
            throw new InvalidDataException("Ciclista já possui um aluguel ativo.");
        }

        // Valida a tranca
        Tranca tranca = trancaService.obterTranca(idTranca);
        if (!StatusTranca.OCUPADA.equals(tranca.getStatus())) {
            log.warn("Tranca com status inválido. ID Tranca: {}", idTranca);
            throw new InvalidDataException("Tranca não está ocupada.");
        }

        // Lê a bicicleta presa na tranca
        if(tranca.getBicicleta() == null) {
            log.error("Tranca sem bicicleta presa. ID Tranca: {}", idTranca);
            throw new ResourceNotFoundException("Tranca sem bicicleta presa.");
        }
        Bicicleta bicicleta = tranca.getBicicleta();
        if (!StatusBicicleta.DISPONIVEL.equals(bicicleta.getStatus())) {
            log.warn("Bicicleta com status inválido. ID Bicicleta: {}", bicicleta.getId());
            throw new BadRequestException("Bicicleta não está disponível.");
        }

        // Verifica se a bicicleta não está em reparo
        if (StatusBicicleta.EM_REPARO.equals(bicicleta.getStatus())) {
            log.warn("Bicicleta em reparo. ID Bicicleta: {}", bicicleta.getId());
            throw new BadRequestException("Bicicleta não pode ser alugada.");
        }

        // Envia a cobrança para a Administradora CC (R2)
        NovoCobrancaDTO cobranca = new NovoCobrancaDTO();
        cobranca.setCiclista(idCiclista);
        cobranca.setValor(10.0);
        boolean pagamentoAutorizado = administradoraCCService.enviarCobranca(cobranca);
        if (!pagamentoAutorizado) {
            log.error("Pagamento não autorizado para ciclista: {}", idCiclista);
            // Registra a cobrança para ser cobrada posteriormente (E3)
            administradoraCCService.registrarCobrancaPendente(idCiclista);
            throw new BadRequestException("Pagamento não autorizado.");
        }

        // Registra os dados da retirada da bicicleta (R3)
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(idCiclista);
        aluguel.setBicicleta(Integer.parseInt(bicicleta.getId().toString()));
        aluguel.setTrancaInicio(idTranca);
        aluguel.setHoraInicio(LocalDateTime.now().toString());

         /*
         * Altera o status da bicicleta para "em uso"
         * Solicita abertura da tranca e altera status para "livre"
         * */
        trancaService.destrancarTranca(idTranca, bicicleta.getId());

        // Envia uma mensagem para o ciclista com os dados do aluguel (R4)
        emailService.enviarEmailAluguel(idCiclista, aluguel, bicicleta, tranca);

        aluguelRepository.save(aluguel);
        log.info("Aluguel realizado com sucesso para ciclista: {}", idCiclista);
        return aluguel;
    }
}
