package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;

public class Devolucao {

    public int id;

    public String nome;

    public String dataNascimento;

    public StatusCiclista status;

    public Devolucao cpf;

    public String email;

    public String urlFotoDocumento;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public StatusCiclista getStatus() {
        return status;
    }

    public void setStatus(StatusCiclista status) {
        this.status = status;
    }

    public Devolucao getCpf() {
        return cpf;
    }

    public void setCpf(Devolucao cpf) {
        this.cpf = cpf;
    }


}
