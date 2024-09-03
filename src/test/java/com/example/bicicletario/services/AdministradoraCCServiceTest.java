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
    }

    @Test
    void validarCartao_Success() {
        // Arrange
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("1234567890123456");

        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act & Assert
        assertDoesNotThrow(() -> administradoraCCService.validarCartao(cartaoDeCredito));
    }

    @Test
    void validarCartao_Failure() {
        // Arrange
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("1234567890123456");

        doThrow(new RuntimeException()).when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> administradoraCCService.validarCartao(cartaoDeCredito));
    }

    @Test
    void enviarCobranca_Success() {
        // Arrange
        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        cobrancaDTO.setValor(100.0);

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        boolean result = administradoraCCService.enviarCobranca(cobrancaDTO);

        // Assert
        assertTrue(result);
    }

    @Test
    void enviarCobranca_Failure() {
        // Arrange
        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        cobrancaDTO.setValor(100.0);

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
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

        doThrow(new RuntimeException()).when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> administradoraCCService.enviarCobranca(cobrancaDTO));
    }
}
