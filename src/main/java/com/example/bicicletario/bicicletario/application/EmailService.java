package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.ASSUNTO_EMAIL_REPARADOR;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.EMAIL_ENVIADO_PARA_O_REPARADOR;

@Service
public class EmailService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EmailService.class);
    private final FuncionarioService funcionarioService;

    public EmailService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }


    public void enviarEmail(String email, String assunto, String mensagem) {
        logger.info(String.format("Email enviado para: %s com assunto: %s e mensagem: %s", email, assunto, mensagem));
    }

    public void enviarEmailParaReparador(Long idFuncionario) {
        Funcionario funcionario = funcionarioService.get(idFuncionario);
        enviarEmail(funcionario.getEmail(), ASSUNTO_EMAIL_REPARADOR, EMAIL_ENVIADO_PARA_O_REPARADOR);
    }
}
