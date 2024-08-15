package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.domain.Email;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoEmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviarEmail")
    public ResponseEntity<Object>  enviarEmail(@RequestBody NovoEmailDTO novoEmailDTO) {
        try {
            logger.info("Envio de email");
            Email email = emailService.enviarEmail(novoEmailDTO);
            logger.info("Email enviado com sucesso");
            return ResponseEntity.status(200).body(email);
        } catch (Exception e) {
            if (e.getMessage().contains("E-mail com formato invalido")) {
                Erro erro = new Erro("422", "E-mail com formato invalido");
                logger.error("{} - {}", erro.getCodigo(), erro.getMensagem());
                return ResponseEntity.status(422).body(erro);
            }
            Erro erro = new Erro("404", "E-mail nao existe");
            logger.error("{} - {}", erro.getCodigo(), erro.getMensagem());
            return ResponseEntity.status(404).body(erro);
        }
    }
}