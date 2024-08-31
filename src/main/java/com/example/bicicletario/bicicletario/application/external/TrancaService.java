package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TrancaService {
    private static final Logger log = LoggerFactory.getLogger(TrancaService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${equipamento.service.url}")
    private String baseUrl;

    public void atualizarStatusTranca(int idTranca, StatusTranca status) {
        String url = baseUrl + "/trancas/" + idTranca + "/status";
        log.info("Atualizando status da tranca {} para {}", idTranca, status);
        restTemplate.put(url, status);
    }

    public Optional<NovoTrancaDTO> obterTranca(int idTranca) {
        String url = baseUrl + "/trancas/" + idTranca;
        try {
            NovoTrancaDTO tranca = restTemplate.getForObject(url, NovoTrancaDTO.class);
            return Optional.ofNullable(tranca);
        } catch (Exception e) {
            log.error("Erro ao obter a tranca com ID {}", idTranca, e);
            return Optional.empty();
        }
    }

    public void prenderBicicleta(int idBicicleta, int idTranca) {
        String url = baseUrl + "/trancas/" + idTranca + "/prender";
        log.info("Prendendo bicicleta {} na tranca {}", idBicicleta, idTranca);
        restTemplate.postForObject(url, idBicicleta, Void.class);
    }
}
