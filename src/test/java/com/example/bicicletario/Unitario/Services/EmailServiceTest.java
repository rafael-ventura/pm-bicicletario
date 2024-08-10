package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class EmailServiceTest {

    @Mock
    private FuncionarioService funcionarioService;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(funcionarioService);
    }

    @Test
    void enviarEmailParaReparador_FuncionarioNotFound() {
        when(funcionarioService.get(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaReparador(1L);
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }

    @Test
    void enviarEmail_EmailInvalido() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmail(null, "Assunto", "Mensagem");
        });

        assertEquals("Endereço de e-mail inválido.", exception.getMessage());

        exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmail("", "Assunto", "Mensagem");
        });

        assertEquals("Endereço de e-mail inválido.", exception.getMessage());
    }

    @Test
    void enviarEmail_ErroAoEnviarEmail() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emailService.enviarEmail("erro@teste.com", "Assunto", "Mensagem");
        });

        assertEquals("Erro ao enviar e-mail", exception.getMessage());
    }

    @Test
    void enviarEmail_Sucesso() {
        emailService.enviarEmail("teste@teste.com", "Assunto", "Mensagem");

        // Simulação de envio bem-sucedido; nada precisa ser verificado aqui
    }
}
