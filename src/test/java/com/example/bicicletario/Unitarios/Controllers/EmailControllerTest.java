package com.example.bicicletario.Unitarios.Controllers;

import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.domain.Email;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoEmailDTO;
import com.example.bicicletario.bicicletario.web.EmailController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarEmail_Success() {
        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
        novoEmailDTO.setEmail("teste@example.com");
        novoEmailDTO.setAssunto("Teste");
        novoEmailDTO.setMensagem("Mensagem de teste");

        Email email = new Email();
        email.setId(1);
        email.setEmail(novoEmailDTO.getEmail());
        email.setAssunto(novoEmailDTO.getAssunto());
        email.setMensagem(novoEmailDTO.getMensagem());

        when(emailService.enviarEmail(novoEmailDTO)).thenReturn(email);

        ResponseEntity<Object> response = emailController.enviarEmail(novoEmailDTO);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(email.getId(), ((Email) response.getBody()).getId());
        assertEquals(email.getEmail(), ((Email) response.getBody()).getEmail());
    }

    @Test
    void enviarEmail_EmailInvalido() {
        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
        novoEmailDTO.setEmail("email_invalido");
        novoEmailDTO.setAssunto("Teste");
        novoEmailDTO.setMensagem("Mensagem de teste");

        // Usar RuntimeException ao invés de Exception
        when(emailService.enviarEmail(novoEmailDTO)).thenThrow(new RuntimeException("E-mail com formato invalido"));

        ResponseEntity<Object> response = emailController.enviarEmail(novoEmailDTO);

        assertEquals(422, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("422", ((Erro) response.getBody()).getCodigo());
        assertEquals("E-mail com formato invalido", ((Erro) response.getBody()).getMensagem());
    }

    @Test
    void enviarEmail_EmailNaoExiste() {
        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
        novoEmailDTO.setEmail("inexistente@example.com");
        novoEmailDTO.setAssunto("Teste");
        novoEmailDTO.setMensagem("Mensagem de teste");

        // Usar RuntimeException ao invés de Exception
        when(emailService.enviarEmail(novoEmailDTO)).thenThrow(new RuntimeException("E-mail nao existe"));

        ResponseEntity<Object> response = emailController.enviarEmail(novoEmailDTO);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("404", ((Erro) response.getBody()).getCodigo());
        assertEquals("E-mail nao existe", ((Erro) response.getBody()).getMensagem());
    }
}
