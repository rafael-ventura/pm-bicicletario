package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.exception.NotFoundException;
import com.example.bicicletario.bicicletario.domain.Email;
import com.example.bicicletario.bicicletario.domain.dto.NovoEmailDTO;
import com.example.bicicletario.bicicletario.infraestructure.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(JavaMailSender mailSender, EmailRepository emailRepository) {
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
    }


    public Email enviarEmail(NovoEmailDTO novoEmailDTO) throws Exception {
        // Validação básica de e-mail
        if (!novoEmailDTO.getEmail().contains("@")) {
            throw new Exception("E-mail com formato invalido");
        }

        // Criação do objeto Email
        Email email = new Email();
        email.setEmail(novoEmailDTO.getEmail());
        email.setAssunto(novoEmailDTO.getAssunto());
        email.setMensagem(novoEmailDTO.getMensagem());

        try {
            // Enviar o e-mail
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(novoEmailDTO.getEmail());
            message.setSubject(novoEmailDTO.getAssunto());
            message.setText(novoEmailDTO.getMensagem());
            mailSender.send(message);

            // Salvar o e-mail no repositório
            email = emailRepository.save(email);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }

        return email;
    }
}
