package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BicicletaService {
    private static final Logger log = LoggerFactory.getLogger(BicicletaService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${equipamento.base-url}")
    private String baseUrl;

    public Bicicleta getBicicletaById(int idBicicleta) {
        String url = baseUrl + "/bicicleta/" + idBicicleta;
        try {
            ResponseEntity<Bicicleta> response = restTemplate.getForEntity(url, Bicicleta.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Bicicleta não encontrada com ID {}", idBicicleta, e);
            throw new ResourceNotFoundException("Não encontrado");
        } catch (Exception e) {
            log.error("Erro ao obter a bicicleta com ID {}", idBicicleta, e);
            throw new ResourceNotFoundException("Erro ao obter a bicicleta.");
        }
    }

    public void atualizarStatus(Bicicleta bicicleta, StatusBicicleta status) {
        String url = baseUrl + "/bicicleta/" + bicicleta.getId() + "/status";
        log.info("Atualizando status da bicicleta {} para {}", bicicleta.getId(), status);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<StatusBicicleta> entity = new HttpEntity<>(status, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }
}