package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class TrancaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrancaService trancaService;

    @Value("${equipamento.base-url}")
    private String baseUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Manually injecting the baseUrl if it's not already injected.
        baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8060/api";
        trancaService = new TrancaService(restTemplate, baseUrl);
    }

    @Test
    void trancarTranca_Success() {
        // Arrange
        Tranca mockTranca = new Tranca();
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        String expectedUrl = baseUrl + "/tranca/1/trancar";
        when(restTemplate.postForEntity(eq(expectedUrl), any(HttpEntity.class), eq(Tranca.class)))
                .thenReturn(responseEntity);

        // Act
        Tranca result = trancaService.trancarTranca(1, 2);

        // Assert
        assertNotNull(result);
        assertEquals(mockTranca, result);
    }

    @Test
    void trancarTranca_ResourceNotFound() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenThrow(new ResourceNotFoundException("Não encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.trancarTranca(1, 2));
    }

    @Test
    void trancarTranca_InvalidDataException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenThrow(new InvalidDataException("Dados Inválidos"));

        assertThrows(InvalidDataException.class, () -> trancaService.trancarTranca(1, 2));
    }

    @Test
    void obterTranca_Success() {
        Tranca mockTranca = new Tranca();  // Crie o mock da resposta esperada
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenReturn(responseEntity);

        Tranca result = trancaService.obterTranca(1);

        assertNotNull(result);
        assertEquals(mockTranca, result);
    }

    @Test
    void obterTranca_ResourceNotFound() {
        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.obterTranca(1));
    }
}
