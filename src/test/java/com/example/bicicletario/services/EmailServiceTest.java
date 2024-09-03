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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private final String baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8060/api";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            var field = EmailService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(emailService, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void enviarEmail_Success() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<EmailDTO> request = new HttpEntity<>(email, headers);

        // Simulando a resposta do endpoint com sucesso
        when(restTemplate.postForEntity(eq(baseUrl + "/enviarEmail"), eq(request), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        boolean result = emailService.enviarEmail(email);

        // Assert
        assertTrue(result);
        verify(restTemplate, times(1)).postForEntity(eq(baseUrl + "/enviarEmail"), eq(request), eq(Void.class));
    }

    @Test
    void enviarEmail_Failure() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<EmailDTO> request = new HttpEntity<>(email, headers);

        // Simulando a resposta do endpoint com erro
        when(restTemplate.postForEntity(eq(baseUrl + "/enviarEmail"), eq(request), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        boolean result = emailService.enviarEmail(email);

        // Assert
        assertFalse(result);
        verify(restTemplate, times(1)).postForEntity(eq(baseUrl + "/enviarEmail"), eq(request), eq(Void.class));
    }

    @Test
    void enviarEmail_Exception() {
        // Arrange
        EmailDTO email = new EmailDTO();
        email.setEmail("test@bicicletario.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<EmailDTO> request = new HttpEntity<>(email, headers);

        // Simulando uma exceção ao chamar o endpoint
        doThrow(new RuntimeException("Simulated Exception")).when(restTemplate).postForEntity(eq(baseUrl + "/enviarEmail"), eq(request), eq(Void.class));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailService.enviarEmail(email);
        });

        assertEquals("Erro ao enviar email.", exception.getMessage());
        verify(restTemplate, times(1)).postForEntity(eq(baseUrl + "/enviarEmail"), eq(request), eq(Void.class));
    }

    @Test
    void enviarEmailAluguel_ComSucesso() {
        // Arrange
        var idCiclista = 1;
        var aluguel = mock(Aluguel.class);
        var bicicleta = mock(Bicicleta.class);
        var tranca = mock(Tranca.class);

        when(bicicleta.getNumero()).thenReturn(1);
        when(bicicleta.getMarca()).thenReturn("MarcaX");
        when(bicicleta.getModelo()).thenReturn("ModeloY");
        when(tranca.getId()).thenReturn(2);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailAluguel(idCiclista, aluguel, bicicleta, tranca);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    /*@Test
    void enviarEmailAluguelExistente_ComSucesso() {
        // Arrange
        var idCiclista = 1;
        var bicicleta = mock(Bicicleta.class);
        var aluguel = mock(Aluguel.class);

        when(aluguel.getBicicleta()).thenReturn(bicicleta.getId());
        when(aluguel.getHoraInicio()).thenReturn("10:00");
        when(aluguel.getTrancaInicio()).thenReturn(1);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailAluguelExistente(idCiclista, aluguel);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }*/

    @Test
    void enviarEmailDevolucao_ComSucesso() {
        // Arrange
        var idCiclista = 1;
        var aluguel = mock(Aluguel.class);
        var bicicleta = mock(Bicicleta.class);
        var tranca = mock(Tranca.class);

        when(aluguel.getHoraFim()).thenReturn("11:00");
        when(bicicleta.getNumero()).thenReturn(1);
        when(bicicleta.getMarca()).thenReturn("MarcaX");
        when(bicicleta.getModelo()).thenReturn("ModeloY");
        when(tranca.getId()).thenReturn(2);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailDevolucao(idCiclista, aluguel, bicicleta, tranca, 10.0, "1234", "Pago", "12:00");

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }
}
