package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        // Arrange
        int idTranca = 1;
        int idBicicleta = 1;
        Tranca tranca = new Tranca();

        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenReturn(new ResponseEntity<>(tranca, HttpStatus.OK));

        // Act
        Tranca result = trancaService.trancarTranca(idTranca, idBicicleta);

        // Assert
        assertNotNull(result);
    }

    @Test
    void trancarTranca_NotFound() {
        // Arrange
        int idTranca = 1;
        int idBicicleta = 1;

        when(restTemplate.postForEntity(anyString(), any(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            trancaService.trancarTranca(idTranca, idBicicleta);
        });

        assertEquals("Não encontrado", exception.getMessage());
    }

    @Test
    void destrancarTranca_Success() {
        // Arrange
        int idTranca = 1;
        int idBicicleta = 1;

        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act
        trancaService.destrancarTranca(idTranca, idBicicleta);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void destrancarTranca_NotFound() {
        // Arrange
        int idTranca = 1;
        int idBicicleta = 1;

        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(restTemplate)
                .postForEntity(anyString(), any(), eq(Void.class));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            trancaService.destrancarTranca(idTranca, idBicicleta);
        });

        assertEquals("Não encontrado", exception.getMessage());
    }

    @Test
    void obterTranca_Success() {
        // Arrange
        int idTranca = 1;
        Tranca tranca = new Tranca();

        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenReturn(new ResponseEntity<>(tranca, HttpStatus.OK));

        // Act
        Tranca result = trancaService.obterTranca(idTranca);

        // Assert
        assertNotNull(result);
    }

    @Test
    void obterTranca_NotFound() {
        // Arrange
        int idTranca = 1;

        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            trancaService.obterTranca(idTranca);
        });

        assertEquals("Tranca não encontrada com ID " + idTranca, exception.getMessage());
    }

    @Test
    void prenderBicicleta_Success() {
        // Arrange
        int idTranca = 1;
        int idBicicleta = 1;

        doNothing().when(restTemplate).postForObject(anyString(), any(), eq(Void.class));

        // Act
        trancaService.prenderBicicleta(idBicicleta, idTranca);

        // Assert
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Void.class));
    }

    @Test
    void getBicicletaByTranca_Success() {
        // Arrange
        int trancaFim = 1;
        Bicicleta bicicleta = new Bicicleta();

        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenReturn(new ResponseEntity<>(bicicleta, HttpStatus.OK));

        // Act
        Bicicleta result = trancaService.getBicicletaByTranca(trancaFim);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getBicicletaByTranca_NotFound() {
        // Arrange
        int trancaFim = 1;

        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            trancaService.getBicicletaByTranca(trancaFim);
        });

        assertEquals("Bicicleta não encontrada.", exception.getMessage());
    }
}