package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciclista")
public class CartaoDeCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "modelo")
    public String nome;

    @Column(name = "ano")
    public String dataNascimento;

    @Column(name = "status")
    public StatusCiclista status;

    @JoinColumn(name = "passaporte_id")
    @OneToOne
    public CartaoDeCredito cpf;

    @Column(name = "email")
    public String email;

    @Column(name = "url_foto_documento")
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

    public CartaoDeCredito getCpf() {
        return cpf;
    }

    public void setCpf(CartaoDeCredito cpf) {
        this.cpf = cpf;
    }


}
