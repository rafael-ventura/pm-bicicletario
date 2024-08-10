package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrancaService {
    public void atualizarStatusTranca(int idTranca, String livre) {
        // Atualiza o status da tranca
    }

    public Optional<NovoTrancaDTO> obterTranca(int idTranca) {
        // Retorna a tranca
        NovoTrancaDTO tranca = new NovoTrancaDTO();
        tranca.setLocalizacao("Localização da tranca");
        tranca.setStatus("Livre");
        return Optional.of(tranca);
    }

    public void prenderBicicleta(int idTranca, int idBicicleta) {
        // Prende a bicicleta na tranca
    }
}
