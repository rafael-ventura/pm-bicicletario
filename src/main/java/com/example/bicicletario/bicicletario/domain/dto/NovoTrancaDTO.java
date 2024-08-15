package com.example.bicicletario.bicicletario.domain.dto;

import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;

public class NovoTrancaDTO {
    private int id;
    private String localizacao;
    private StatusTranca status;
    private int bicicleta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public StatusTranca getStatus() {
        return status;
    }

    public void setStatus(StatusTranca status) {
        this.status = status;
    }

    public int getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(int bicicleta) {
        this.bicicleta = bicicleta;
    }
}
