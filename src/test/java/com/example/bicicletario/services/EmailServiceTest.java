package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
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

class EmailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarEmail_Success() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        // Simulando a resposta do endpoint com sucesso
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        boolean result = emailService.enviarEmail(email);

        // Assert
        assertTrue(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmail_Failure() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        // Simulando a resposta do endpoint com erro
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        boolean result = emailService.enviarEmail(email);

        // Assert
        assertFalse(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmail_Exception() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        // Simulando uma exceção ao chamar o endpoint
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailService.enviarEmail(email);
        });

        assertEquals("Erro ao enviar email.", exception.getMessage());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmailAluguel_Success() {
        // Arrange
        int idCiclista = 1;
        Aluguel aluguel = new Aluguel();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(1234);
        bicicleta.setMarca("MarcaTest");
        bicicleta.setModelo("ModeloTest");
        Tranca tranca = new Tranca();
        tranca.setId(1);

        // Simulando uma chamada bem-sucedida ao endpoint
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailAluguel(idCiclista, aluguel, bicicleta, tranca);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }
}

