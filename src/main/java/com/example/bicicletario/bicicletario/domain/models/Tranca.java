package com.example.bicicletario.bicicletario.domain.models;

import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import jakarta.persistence.*;

@Entity
@Table
public class Tranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private StatusTranca status;

    @Column(nullable = false, unique = true)
    private int numero;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private String anoDeFabricacao;

    @Column(nullable = false)
    private String modelo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusTranca getStatus() {
        return status;
    }

    public void setStatus(StatusTranca status) {
        this.status = status;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getAnoDeFabricacao() {
        return anoDeFabricacao;
    }

    public void setAnoDeFabricacao(String anoDeFabricacao) {
        this.anoDeFabricacao = anoDeFabricacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
