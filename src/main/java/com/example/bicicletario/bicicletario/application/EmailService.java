package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.springframework.stereotype.Service;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.ASSUNTO_EMAIL_REPARADOR;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.EMAIL_ENVIADO_PARA_O_REPARADOR;

@Service
public class EmailService {

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
            System.out.printf("Email enviado para: %s com assunto: %s e mensagem: %s%n", email, assunto, mensagem);
        } catch (Exception e) {
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
