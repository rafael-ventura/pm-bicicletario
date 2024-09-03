package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdministradoraCCServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AdministradoraCCService administradoraCCService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            var field = AdministradoraCCService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(administradoraCCService, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void validarCartao_ComSucesso() {
        // Arrange
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("1234567890123456");

        doNothing().when(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));

        // Act & Assert
        assertDoesNotThrow(() -> administradoraCCService.validarCartao(cartaoDeCredito));
    }

    @Test
    void validarCartao_Falha() {
        // Arrange
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("1234567890123456");

        doThrow(new RuntimeException()).when(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> administradoraCCService.validarCartao(cartaoDeCredito));
    }

    @Test
    void enviarCobranca_ComSucesso() {
        // Arrange
        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        cobrancaDTO.setValor(100.0);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        boolean result = administradoraCCService.enviarCobranca(cobrancaDTO);

        // Assert
        assertTrue(result);
    }

    @Test
    void enviarCobranca_Falha() {
        // Arrange
        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        cobrancaDTO.setValor(100.0);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        boolean result = administradoraCCService.enviarCobranca(cobrancaDTO);

        // Assert
        assertFalse(result);
    }

    @Test
    void enviarCobranca_Exception() {
        // Arrange
        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        cobrancaDTO.setValor(100.0);

        doThrow(new RuntimeException()).when(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> administradoraCCService.enviarCobranca(cobrancaDTO));
    }

    @Test
    void registrarCobrancaPendente_ComSucesso() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        assertDoesNotThrow(() -> administradoraCCService.registrarCobrancaPendente(1));

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void registrarCobrancaPendente_Falha() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> administradoraCCService.registrarCobrancaPendente(1));

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void registrarCobrancaPendente_Exception() {
        // Arrange
        doThrow(new RuntimeException()).when(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> administradoraCCService.registrarCobrancaPendente(1));

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }
}
