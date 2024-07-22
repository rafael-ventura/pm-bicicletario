package com.example.bicicletario.bicicletario.application;

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

    public boolean enviarEmail(String email, String assunto, String mensagem) {
        System.out.println("Email enviado para: " + email + " com assunto: " + assunto + " e mensagem: " + mensagem);
        return true;
    }

    boolean enviarEmailParaReparador(Long idFuncionario) {
        Funcionario funcionario = funcionarioService.get(idFuncionario);
        return enviarEmail(funcionario.getEmail(), ASSUNTO_EMAIL_REPARADOR, EMAIL_ENVIADO_PARA_O_REPARADOR);
    }
}
