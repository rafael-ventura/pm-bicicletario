package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.ASSUNTO_EMAIL_REPARADOR;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.EMAIL_ENVIADO_PARA_O_REPARADOR;

@Service
public class EmailService {

    @Autowired
    private FuncionarioService funcionarioService;


    public void enviarEmail(String email, String assunto, String mensagem) {
        System.out.println("Email enviado para: " + email + " com assunto: " + assunto + " e mensagem: " + mensagem);
    }

    public void enviarEmailParaReparador(Long idFuncionario) {
        Funcionario funcionario = funcionarioService.get(idFuncionario);
        enviarEmail(funcionario.getEmail(), ASSUNTO_EMAIL_REPARADOR, EMAIL_ENVIADO_PARA_O_REPARADOR);
    }
}
