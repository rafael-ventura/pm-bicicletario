package com.example.bicicletario.bicicletario.application;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.CobrancaRepository;
import com.example.bicicletario.bicicletario.infraestructure.FilaCobrancaRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilaCobrancaService {

    private final FilaCobrancaRepository filaCobrancaRepository;
    private final CobrancaService cobrancaService;
    private final CobrancaRepository cobrancaRepository;
    public FilaCobrancaService(FilaCobrancaRepository filaCobrancaRepository,
                               CobrancaService cobrancaService,
                               CobrancaRepository cobrancaRepository) {
        this.filaCobrancaRepository = filaCobrancaRepository;
        this.cobrancaService = cobrancaService;
        this.cobrancaRepository = cobrancaRepository;
    }

    public Cobranca adicionarNaFila(NovoCobrancaDTO novaCobranca) {
        // Validação do valor
        if (novaCobranca.getValor() == null || novaCobranca.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }

        // Criação do objeto Cobranca
        Cobranca cobranca = new Cobranca();
        cobranca.setCiclista(novaCobranca.getCiclista());
        cobranca.setValor(novaCobranca.getValor());
        cobranca.setStatusCobranca(StatusCobranca.PENDENTE);
        cobranca.setHoraSolicitacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        cobranca = cobrancaRepository.save(cobranca);

        // Adicionar a cobrança na fila
        filaCobrancaRepository.adicionarNaFila(cobranca);

        return cobranca;
    }

    public List<Cobranca> processarFila() {
        List<Cobranca> cobrancasProcessadas = new ArrayList<>();

        while (!filaCobrancaRepository.isEmpty()) {
            Cobranca cobranca = filaCobrancaRepository.removerDaFila();
            NovoCobrancaDTO novoCobrancaDTO = new NovoCobrancaDTO();
            novoCobrancaDTO.setCiclista(cobranca.getCiclista());
            novoCobrancaDTO.setValor(cobranca.getValor());
            Cobranca cobrancaProcessada = cobrancaService.realizarCobranca(novoCobrancaDTO);

            if (!cobrancaProcessada.getStatusCobranca().equals(StatusCobranca.PAGA)) {
                cobranca.setStatusCobranca(StatusCobranca.FALHA);
                cobranca.setHoraFinalizacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                adicionarNaFila(novoCobrancaDTO);
            }
            cobrancasProcessadas.add(cobrancaProcessada);
        }

        return cobrancasProcessadas;
    }


}
