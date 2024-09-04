package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    // Simulando a URL base como uma variável de instância
    private final String baseUrl = "http://localhost:8080/api";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void testDestrancarTranca_Success() {
        // Arrange
        int trancaId = 1;
        Integer bicicletaId = 2;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<Integer> request = new HttpEntity<>(bicicletaId, headers);

        when(restTemplate.postForEntity(eq(baseUrl + "/tranca/" + trancaId + "/destrancar"), eq(request), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        trancaService.destrancarTranca(trancaId, bicicletaId);

        // Assert
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
    void testObterTranca_Success() {
        // Arrange
        int trancaId = 1;
        Tranca tranca = new Tranca();
        tranca.setId(trancaId);
        tranca.setStatus(StatusTranca.LIVRE);  // Exemplo de status para a tranca

        when(restTemplate.getForEntity(anyString(), eq(Tranca.class)))
                .thenReturn(new ResponseEntity<>(tranca, HttpStatus.OK));

        // Act
        Tranca result = trancaService.obterTranca(trancaId);

        // Assert
        assertNotNull(result);
        assertEquals(trancaId, result.getId());
        assertEquals(StatusTranca.LIVRE, result.getStatus());  // Verifica se o status foi corretamente atribuído
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
    void testPrenderBicicleta_Success() {
        // Arrange
        int bicicletaId = 1;
        int trancaId = 2;

        when(restTemplate.postForObject(eq(baseUrl + "/tranca/" + trancaId + "/prender"), eq(bicicletaId), eq(Void.class)))
                .thenReturn(null);

        // Act
        trancaService.prenderBicicleta(bicicletaId, trancaId);

        // Assert
        verify(restTemplate, times(1)).postForObject(anyString(), eq(bicicletaId), eq(Void.class));
    }


}
