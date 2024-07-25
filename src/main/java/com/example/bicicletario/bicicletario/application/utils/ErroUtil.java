package com.example.bicicletario.bicicletario.application.utils;

import com.example.bicicletario.bicicletario.domain.models.Erro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroUtil {

    public ErroUtil() {
        throw new IllegalStateException("Classe utilitária");
    }

    public static ResponseEntity<Erro> criarErro(HttpStatus status, String codigo, String mensagem) {
        Erro erro = new Erro(codigo, mensagem);
        return new ResponseEntity<>(erro, status);
    }

    public static ResponseEntity<Erro> erroInterno(String mensagem) {
        return criarErro(HttpStatus.INTERNAL_SERVER_ERROR, "500", mensagem);
    }

    public static ResponseEntity<Erro> erroNaoEncontrado(String mensagem) {
        return criarErro(HttpStatus.NOT_FOUND, "404", mensagem);
    }

    public static ResponseEntity<Erro> erroInvalido(String mensagem) {
        return criarErro(HttpStatus.UNPROCESSABLE_ENTITY, "422", mensagem);
    }
}
