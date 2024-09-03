package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.EmailService;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FuncionarioService funcionarioService;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(funcionarioService);
        try {
            var field = EmailService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(emailService, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void enviarEmail_ComSucesso() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmail("test@example.com", "Assunto", "Mensagem");

        // Assert
        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmail_ComErroDeServidor_DeveLancarBadRequestException() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            emailService.enviarEmail("test@example.com", "Assunto", "Mensagem");
        });

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailParaReparador_ComSucesso() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setEmail("reparador@example.com");

        when(funcionarioService.get(anyInt()))
                .thenReturn(funcionario);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailParaReparador(1, "Assunto", "Mensagem");

        // Assert
        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailParaReparador_FuncionarioNaoEncontrado_DeveLancarResourceNotFoundException() {
        // Arrange
        when(funcionarioService.get(anyInt()))
                .thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaReparador(1, "Assunto", "Mensagem");
        });

        verify(funcionarioService, times(1)).get(anyInt());
        verify(restTemplate, times(0)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailParaBicicleta_ComSucesso() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(123);
        bicicleta.setMarca("MarcaTest");
        bicicleta.setModelo("ModeloTest");
        bicicleta.setAno("2023");
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);
        Tranca tranca = new Tranca();
        tranca.setId(10);
        tranca.setNumero(123);
        tranca.setModelo("ModeloTest");
        tranca.setAnoDeFabricacao("2023");
        tranca.setStatus(StatusTranca.OCUPADA);


        when(funcionarioService.get(anyInt()))
                .thenReturn(funcionario);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailParaBicicleta(1, bicicleta, tranca, "Inclusão");

        // Assert
        verify(funcionarioService, times(1)).get(anyInt());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailParaTranca_ComSucesso() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        Tranca tranca = new Tranca();
        tranca.setNumero(123);
        tranca.setModelo("ModeloTest");
        tranca.setAnoDeFabricacao("2023");
        tranca.setStatus(StatusTranca.OCUPADA);

        when(funcionarioService.get(anyInt()))
                .thenReturn(funcionario);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        emailService.enviarEmailParaTranca(1, tranca, "Remoção");

        // Assert
        verify(funcionarioService, times(1)).get(anyInt());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmail_ComExceptionGenerica_DeveLancarBadRequestException() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new RuntimeException("Erro genérico"));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            emailService.enviarEmail("test@example.com", "Assunto", "Mensagem");
        });

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }
}
