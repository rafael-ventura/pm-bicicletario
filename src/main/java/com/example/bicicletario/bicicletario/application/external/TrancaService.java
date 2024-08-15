package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrancaService {
    private static final Logger log = LoggerFactory.getLogger(TrancaService.class);

    public void atualizarStatusTranca(int idTranca, StatusTranca status) {
        // Atualiza o status da tranca
        log.info("Atualizando status da tranca {} para {}", idTranca, status);
    }

    public Optional<NovoTrancaDTO> obterTranca(int idTranca) {
        // Retorna a tranca
        NovoTrancaDTO tranca = new NovoTrancaDTO();
        tranca.setLocalizacao("Localização da tranca");
        tranca.setStatus(StatusTranca.LIVRE);
        return Optional.of(tranca);
    }

    public void prenderBicicleta(int idTranca, int idBicicleta) {
        // Prende a bicicleta na tranca
    }
}
