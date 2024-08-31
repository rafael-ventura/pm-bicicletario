package com.example.bicicletario.bicicletario.domain.dto;

public class NovoCobrancaDTO {
    private double valor;
    private int ciclista;

    public NovoCobrancaDTO() {
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getCiclista() {
        return ciclista;
    }

    public void setCiclista(int ciclista) {
        this.ciclista = ciclista;
    }
}
