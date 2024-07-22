package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

public class Ciclista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nascimento")
    private String nascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "nacionalidade")
    private Nacionalidade nacionalidade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passaporte_id", referencedColumnName = "id")
    private Passaporte passaporte;

    @Column(name = "url_foto_documento")
    private String urlFotoDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCiclista statusCiclista;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Passaporte getPassaporte() {
        return passaporte;
    }

    public void setPassaporte(Passaporte passaporte) {
        this.passaporte = passaporte;
    }

    public String getUrlFotoDocumento() {
        return urlFotoDocumento;
    }

    public void setUrlFotoDocumento(String urlFotoDocumento) {
        this.urlFotoDocumento = urlFotoDocumento;
    }

    public StatusCiclista getStatusCiclista() {
        return statusCiclista;
    }

    public void setStatusCiclista(StatusCiclista statusCiclista) {
        this.statusCiclista = statusCiclista;
    }
}
