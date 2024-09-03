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

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        boolean result = emailService.enviarEmail(email);

        // Assert
        assertTrue(result);
    }

    @Test
    void enviarEmail_Failure() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        boolean result = emailService.enviarEmail(email);

        // Assert
        assertFalse(result);
    }

    @Test
    void enviarEmail_Exception() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenThrow(new RuntimeException());

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailService.enviarEmail(email);
        });

        assertEquals("Erro ao enviar email.", exception.getMessage());
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

        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act
        emailService.enviarEmailAluguel(idCiclista, aluguel, bicicleta, tranca);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmailDevolucao_Success() {
        // Arrange
        int idCiclista = 1;
        Aluguel aluguel = new Aluguel();
        aluguel.setHoraFim("2024-09-03 12:00");
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(1234);
        bicicleta.setMarca("MarcaTest");
        bicicleta.setModelo("ModeloTest");
        Tranca tranca = new Tranca();
        tranca.setId(1);
        double valorExtra = 5.0;
        String cartaoUsado = "1234-XXXX-XXXX-5678";
        String statusPagamento = "Pago";
        String dataHoraCobranca = "2024-09-03 12:30";

        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act
        emailService.enviarEmailDevolucao(idCiclista, aluguel, bicicleta, tranca, valorExtra, cartaoUsado, statusPagamento, dataHoraCobranca);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmailAluguelExistente_Success() {
        // Arrange
        int idCiclista = 1;
        Aluguel aluguel = new Aluguel();
        aluguel.setBicicleta(1);
        aluguel.setHoraInicio("2024-09-03 10:00");
        aluguel.setTrancaInicio(1);

        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        // Act
        emailService.enviarEmailAluguelExistente(idCiclista, aluguel);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }
}
