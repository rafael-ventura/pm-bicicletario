package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void trancarTranca_Success() {
        Tranca mockTranca = new Tranca();
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenReturn(responseEntity);

        Tranca result = trancaService.trancarTranca(1, 2);

        assertNotNull(result);
        assertEquals(mockTranca, result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void trancarTranca_ResourceNotFound() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.trancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void trancarTranca_InvalidDataException() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        assertThrows(InvalidDataException.class, () -> trancaService.trancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void destrancarTranca_Success() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);

        assertDoesNotThrow(() -> trancaService.destrancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void destrancarTranca_ResourceNotFound() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.destrancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void obterTranca_Success() {
        Tranca mockTranca = new Tranca();
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenReturn(responseEntity);

        Tranca result = trancaService.obterTranca(1);

        assertNotNull(result);
        assertEquals(mockTranca, result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Tranca.class));
    }

    @Test
    void obterTranca_ResourceNotFound() {
        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.obterTranca(1));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Tranca.class));
    }

    @Test
    void prenderBicicleta_Success() {
        doNothing().when(restTemplate).postForObject(anyString(), anyInt(), eq(Void.class));

        assertDoesNotThrow(() -> trancaService.prenderBicicleta(1, 2));
        verify(restTemplate, times(1)).postForObject(anyString(), anyInt(), eq(Void.class));
    }

    @Test
    void getBicicletaByTranca_Success() {
        Bicicleta mockBicicleta = new Bicicleta();
        ResponseEntity<Bicicleta> responseEntity = new ResponseEntity<>(mockBicicleta, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenReturn(responseEntity);

        Bicicleta result = trancaService.getBicicletaByTranca(1);

        assertNotNull(result);
        assertEquals(mockBicicleta, result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Bicicleta.class));
    }

    @Test
    void getBicicletaByTranca_ResourceNotFound() {
        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> trancaService.getBicicletaByTranca(1));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Bicicleta.class));
    }
}
