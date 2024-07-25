package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.domain.Email;
import com.example.bicicletario.bicicletario.domain.dto.NovoEmailDTO;
import com.example.bicicletario.bicicletario.exception.NotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailRepository emailRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarEmailComSucesso() {
        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
        novoEmailDTO.setEmail("teste@example.com");
        novoEmailDTO.setAssunto("Assunto");
        novoEmailDTO.setMensagem("Mensagem");

        Email email = new Email();
        email.setEmail(novoEmailDTO.getEmail());
        email.setAssunto(novoEmailDTO.getAssunto());
        email.setMensagem(novoEmailDTO.getMensagem());

        when(emailRepository.save(any(Email.class))).thenReturn(email);

        Email resultado = emailService.enviarEmail(novoEmailDTO);

        assertNotNull(resultado);
        assertEquals("teste@example.com", resultado.getEmail());
        assertEquals("Assunto", resultado.getAssunto());
        assertEquals("Mensagem", resultado.getMensagem());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void enviarEmailComFormatoInvalido() {
        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
        novoEmailDTO.setEmail("teste");
        novoEmailDTO.setAssunto("Assunto");
        novoEmailDTO.setMensagem("Mensagem");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.enviarEmail(novoEmailDTO);
        });

        assertEquals("E-mail com formato invalido", exception.getMessage());
        verify(mailSender, times(0)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(0)).save(any(Email.class));
    }

    @Test
    void enviarEmailComErro() {
        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
        novoEmailDTO.setEmail("teste@example.com");
        novoEmailDTO.setAssunto("Assunto");
        novoEmailDTO.setMensagem("Mensagem");

        doThrow(new RuntimeException("Erro ao enviar e-mail")).when(mailSender).send(any(SimpleMailMessage.class));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            emailService.enviarEmail(novoEmailDTO);
        });

        assertEquals("Erro ao enviar e-mail", exception.getMessage());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(0)).save(any(Email.class));
    }
}
