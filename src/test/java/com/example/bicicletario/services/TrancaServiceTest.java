package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class TrancaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trancaService = new TrancaService(objectMapper);
        trancaService.baseUrl = "http://localhost:8080";
    }

    @Test
    void trancarTranca_Success() {
        Tranca mockTranca = new Tranca();  // Crie o mock da resposta esperada
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenReturn(responseEntity);

        Tranca result = trancaService.trancarTranca(1, 2);

        assertNotNull(result);
        assertEquals(mockTranca, result);
    }

    @Test
    void trancarTranca_ResourceNotFound() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.trancarTranca(1, 2));
    }

    @Test
    void trancarTranca_InvalidDataException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.trancarTranca(1, 2));
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
