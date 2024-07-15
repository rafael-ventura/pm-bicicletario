package com.example.bicicletario.bicicletario.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Service
public class EquipamentoClientService {

    @Value("${equipamento.service.url}")
    private String equipamentoServiceUrl;

    private final RestTemplate restTemplate;

    public EquipamentoClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void liberarTranca(Long idTranca, Long idBicicleta) {
        String url = equipamentoServiceUrl + "/tranca/" + idTranca + "/destrancar";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = idBicicleta != null ? "{\"bicicleta\":" + idBicicleta + "}" : "{}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro ao liberar a tranca: " + e.getStatusCode(), e);
        }
    }
}
