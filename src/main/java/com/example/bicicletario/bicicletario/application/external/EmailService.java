package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);
    public void enviarEmail(EmailDTO email) {
        logger.info("Enviando email para " + email.getEmail() + " com a mensagem: " + email.getMensagem() + " e o assunto: " + email.getAssunto());
    }
}
