package com.example.bicicletario.bicicletario.domain.dto;

import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;

public class NovaTrancaDTO {
    private int numero;
    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
    private StatusTranca status;

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

    public StatusTranca getStatus() {
        return status;
    }

    public void setStatus(StatusTranca status) {
        this.status = status;
    }
}