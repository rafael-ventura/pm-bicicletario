package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.EmailService;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void enviarEmailParaBicicleta_FuncionarioNotFound() {
        // Arrange
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaBicicleta(1, bicicleta, tranca, "Inclusão");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
        verify(restTemplate, never()).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmailParaTranca_FuncionarioNotFound() {
        // Arrange
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        Tranca tranca = new Tranca();
        tranca.setNumero(1);
        tranca.setStatus(StatusTranca.LIVRE);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaTranca(1, tranca, "Inclusão");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
        verify(restTemplate, never()).postForEntity(anyString(), any(), eq(Void.class));
    }
}
