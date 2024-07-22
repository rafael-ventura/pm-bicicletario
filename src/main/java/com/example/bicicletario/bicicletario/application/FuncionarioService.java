package com.example.bicicletario.bicicletario.application;


import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FuncionarioService {

    Random random;

    public Funcionario get(long idFuncionario) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario);
        funcionario.setNome("Funcionario");
        funcionario.setEmail("Funcionario@Funcionario.com");
        funcionario.setCpf("123.456.789-00");
        funcionario.setSenha("123456");
        funcionario.setConfirmacaoSenha("123456");
        funcionario.setIdade(30);
        funcionario.setFuncao("Funcionario");
        return funcionario;
    }

    public boolean isFuncionarioValido(long idFuncionario) {
        Funcionario funcionario = get(idFuncionario);
        return funcionario.getId() == idFuncionario;
    }
}
