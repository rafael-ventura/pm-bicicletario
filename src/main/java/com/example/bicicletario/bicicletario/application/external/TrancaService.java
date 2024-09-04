package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TrancaService {

    private static final Logger log = LoggerFactory.getLogger(TrancaService.class);

    // Definição da constante para evitar duplicação
    private static final String TRANCA_BASE_URL = "/tranca/";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public TrancaService(RestTemplate restTemplate, @Value("${equipamento.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Tranca trancarTranca(int trancaId, int bicicletaId) {
        try {
            // Simulação da lógica de trancar a tranca
            String url = baseUrl + TRANCA_BASE_URL + trancaId + "/trancar";
            HttpEntity<Integer> request = new HttpEntity<>(bicicletaId);
            ResponseEntity<Tranca> response = restTemplate.postForEntity(url, request, Tranca.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                throw new InvalidDataException("Dados Inválidos");
            }
            throw new ResourceNotFoundException("Erro ao trancar a tranca.");
        }
    }


    public void destrancarTranca(int idTranca, Integer idBicicleta) {
        String url = baseUrl + TRANCA_BASE_URL + idTranca + "/destrancar";
        log.info("Destrancando a tranca {} com bicicleta {}", idTranca, idBicicleta);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Integer> entity = new HttpEntity<>(idBicicleta, headers);
        try {
            restTemplate.postForEntity(url, entity, Void.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Não encontrado: Tranca {} ou bicicleta {}", idTranca, idBicicleta, e);
            throw new ResourceNotFoundException("Não encontrado");
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("Dados Inválidos: Tranca {} com bicicleta {}", idTranca, idBicicleta, e);
            throw new InvalidDataException("Dados Inválidos");
        } catch (Exception e) {
            log.error("Erro ao destrancar a tranca {} com bicicleta {}", idTranca, idBicicleta, e);
            throw new ResourceNotFoundException("Erro ao destrancar a tranca.");
        }
    }

    public Tranca obterTranca(int idTranca) {
        String url = baseUrl + TRANCA_BASE_URL + idTranca;
        try {
            ResponseEntity<Tranca> response = restTemplate.getForEntity(url, Tranca.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao obter a tranca com ID {}", idTranca, e);
            throw new ResourceNotFoundException("Tranca não encontrada com ID " + idTranca);
        }
    }

    public void prenderBicicleta(int idBicicleta, int idTranca) {
        String url = baseUrl + TRANCA_BASE_URL + idTranca + "/prender";
        log.info("Prendendo bicicleta {} na tranca {}", idBicicleta, idTranca);
        restTemplate.postForObject(url, idBicicleta, Void.class);
    }

    public Bicicleta getBicicletaByTranca(int trancaFim) {
        String url = baseUrl + TRANCA_BASE_URL + trancaFim + "/bicicleta";
        try {
            ResponseEntity<Bicicleta> response = restTemplate.getForEntity(url, Bicicleta.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Bicicleta não encontrada na tranca {}", trancaFim, e);
            throw new ResourceNotFoundException("Bicicleta não encontrada.");
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("Id da tranca inválido: {}", trancaFim, e);
            throw new InvalidDataException("Id da tranca inválido.");
        } catch (Exception e) {
            log.error("Erro ao obter a bicicleta presa na tranca {}", trancaFim, e);
            throw new ResourceNotFoundException("Erro ao obter a bicicleta presa na tranca.");
        }
    }
}
