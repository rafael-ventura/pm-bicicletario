package com.example.bicicletario.bicicletario.domain.dto;

public class NovoDevolucaoDTO {
    private Integer ciclista;
    private Integer trancaFim;

    // Getters and Setters
    public Integer getCiclista() {
        return ciclista;
    }

    public void setCiclista(Integer ciclista) {
        this.ciclista = ciclista;
    }

    public Integer getTrancaFim() {
        return trancaFim;
    }

    public void setTrancaFim(Integer trancaFim) {
        this.trancaFim = trancaFim;
    }
}
