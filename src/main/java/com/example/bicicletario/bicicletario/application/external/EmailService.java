package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {
    public void enviarEmail(EmailDTO email) {
        Logger.getLogger("Enviando email para " + email.getEmail() + " com a mensagem: " + email.getMensagem() + " e o assunto: " + email.getAssunto());
    }
}
