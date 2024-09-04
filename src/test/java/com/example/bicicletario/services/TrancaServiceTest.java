package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.TrancaService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Não nos preocupamos com a URL real neste contexto, apenas queremos simular o comportamento do serviço
    }

    @Test
    void trancarTranca_Success() {
        // Simulando o retorno de sucesso do endpoint
        Tranca mockTranca = new Tranca();
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenReturn(responseEntity);

        // Act
        Tranca result = trancaService.trancarTranca(1, 2);

        // Assert
        assertNotNull(result);
        assertEquals(mockTranca, result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void trancarTranca_ResourceNotFound() {
        // Simulando uma exceção de "não encontrado"
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> trancaService.trancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void trancarTranca_InvalidDataException() {
        // Simulando uma exceção de dados inválidos (422 Unprocessable Entity)
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> trancaService.trancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void destrancarTranca_Success() {
        // Simulando o retorno de sucesso para destrancar a tranca
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);

        // Act & Assert
        assertDoesNotThrow(() -> trancaService.destrancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void destrancarTranca_ResourceNotFound() {
        // Simulando uma exceção de "não encontrado" ao tentar destrancar
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> trancaService.destrancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void destrancarTranca_InvalidDataException() {
        // Simulando uma exceção de dados inválidos ao tentar destrancar
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> trancaService.destrancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void obterTranca_Success() {
        // Simulando o retorno de sucesso ao obter uma tranca
        Tranca mockTranca = new Tranca();
        ResponseEntity<Tranca> responseEntity = new ResponseEntity<>(mockTranca, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenReturn(responseEntity);

        // Act
        Tranca result = trancaService.obterTranca(1);

        // Assert
        assertNotNull(result);
        assertEquals(mockTranca, result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Tranca.class));
    }

    @Test
    void obterTranca_ResourceNotFound() {
        // Simulando uma exceção de "não encontrado" ao tentar obter uma tranca
        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> trancaService.obterTranca(1));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Tranca.class));
    }

    @Test
    void prenderBicicleta_Success() {
        // Simulando o sucesso ao prender uma bicicleta
        when(restTemplate.postForObject(anyString(), anyInt(), eq(Void.class)))
                .thenReturn(null);  // postForObject retorna null para tipos void

        // Act & Assert
        assertDoesNotThrow(() -> trancaService.prenderBicicleta(1, 1));
        verify(restTemplate, times(1)).postForObject(anyString(), anyInt(), eq(Void.class));
    }

    @Test
    void trancarTranca_NullResponse() {
        // Simulando retorno nulo
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> trancaService.trancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Tranca.class));
    }

    @Test
    void destrancarTranca_HttpServerError() {
        // Simulando um erro interno do servidor
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> trancaService.destrancarTranca(1, 2));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

}
