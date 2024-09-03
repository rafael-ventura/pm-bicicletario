package com.example.bicicletario.bicicletario.domain.dto;

import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;

public class NovoCiclistaDTO {
    private String nome;
    private String nascimento;
    private String cpf;
    private PassaporteDTO passaporte;
    private Nacionalidade nacionalidade;
    private String email;
    private String urlFotoDocumento;

    // getters and setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public Nacionalidade getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(Nacionalidade nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public PassaporteDTO getPassaporte() {
        return passaporte;
    }

    public void setPassaporte(PassaporteDTO passaporte) {
        this.passaporte = passaporte;
    }

    public String getUrlFotoDocumento() {
        return urlFotoDocumento;
    }

    public void setUrlFotoDocumento(String urlFotoDocumento) {
        this.urlFotoDocumento = urlFotoDocumento;
    }
}
