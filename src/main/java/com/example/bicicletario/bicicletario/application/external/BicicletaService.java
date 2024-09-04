package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
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
            throw new ResourceNotFoundException("Bicicleta não encontrada com ID " + idBicicleta);
        } catch (Exception e) {
            log.error("Erro ao obter a bicicleta com ID {}", idBicicleta, e);
            throw new ResourceNotFoundException("Erro ao obter a bicicleta com ID " + idBicicleta);
        }
    }

    public void atualizarStatus(Bicicleta bicicleta, StatusBicicleta status) {
        // Constrói a URL de acordo com a especificação Swagger
        String url = baseUrl + "/bicicleta/" + bicicleta.getId() + "/status/" + status.name();
        log.info("Atualizando status da bicicleta {} para {}", bicicleta.getId(), status);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // Faz a requisição POST com a URL ajustada
            restTemplate.exchange(url, HttpMethod.POST, entity, Bicicleta.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Bicicleta não encontrada com ID {}", bicicleta.getId(), e);
            throw new ResourceNotFoundException("Bicicleta não encontrada com ID " + bicicleta.getId());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("Dados inválidos para a bicicleta com ID {}", bicicleta.getId(), e);
            throw new InvalidDataException("Dados inválidos para a bicicleta com ID " + bicicleta.getId());
        } catch (Exception e) {
            log.error("Erro ao atualizar o status da bicicleta com ID {}", bicicleta.getId(), e);
            throw new ResourceNotFoundException("Erro ao atualizar o status da bicicleta.");
        }
    }
}
