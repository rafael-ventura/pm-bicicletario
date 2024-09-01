package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class TrancaService {
    private static final Logger log = LoggerFactory.getLogger(TrancaService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8020/api";


    public void atualizarStatusTranca(int idTranca, String acao) {
        String url = baseUrl + "/tranca/" + idTranca + "/status" + "/acao" + acao;
        log.info("Atualizando status da tranca {} para {}", idTranca, acao);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // headers.set("Authorization", "Bearer " + token); // Caso precise de autenticação

        HttpEntity<String> entity = new HttpEntity<>(acao, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public Optional<NovoTrancaDTO> obterTranca(int idTranca) {
        String url = baseUrl + "/tranca/" + idTranca;
        try {
            NovoTrancaDTO tranca = restTemplate.getForObject(url, NovoTrancaDTO.class);
            return Optional.ofNullable(tranca);
        } catch (Exception e) {
            log.error("Erro ao obter a tranca com ID {}", idTranca, e);
            return Optional.empty();
        }
    }

    public void prenderBicicleta(int idBicicleta, int idTranca) {
        String url = baseUrl + "/tranca/" + idTranca + "/prender";
        log.info("Prendendo bicicleta {} na tranca {}", idBicicleta, idTranca);
        restTemplate.postForObject(url, idBicicleta, Void.class);
    }
}
