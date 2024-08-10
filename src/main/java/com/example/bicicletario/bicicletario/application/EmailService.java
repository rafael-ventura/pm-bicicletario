package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.ASSUNTO_EMAIL_REPARADOR;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.EMAIL_ENVIADO_PARA_O_REPARADOR;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final FuncionarioService funcionarioService;

    public EmailService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    public void enviarEmail(String email, String assunto, String mensagem) {
        if (email == null || email.isEmpty()) {
            throw new ResourceNotFoundException("Endereço de e-mail inválido.");
        }
        try {
            // Simulação de erro para testes
            if (email.equals("erro@teste.com")) {
                throw new InvalidDataException("Erro de envio simulado");
            }
            // Simulação do envio de e-mail
            logger.info("Email enviado para: {} com assunto: {} e mensagem: {}", email, assunto, mensagem);
        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail para: {} com assunto: {} e mensagem: {}", email, assunto, mensagem, e);
            throw new BadRequestException("Erro ao enviar e-mail");
        }
    }

    public void enviarEmailParaReparador(Long idFuncionario) {
        Funcionario funcionario = funcionarioService.get(idFuncionario);
        if (funcionario == null) {
            throw new ResourceNotFoundException("Funcionário não encontrado");
        }
        enviarEmail(funcionario.getEmail(), ASSUNTO_EMAIL_REPARADOR, EMAIL_ENVIADO_PARA_O_REPARADOR);
    }
}
