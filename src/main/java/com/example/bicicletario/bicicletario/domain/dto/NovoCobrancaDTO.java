package com.example.bicicletario.bicicletario.domain.dto;

import java.math.BigDecimal;

public class NovoCobrancaDTO {
    private BigDecimal valor;
    private int ciclista;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getCiclista() {
        return ciclista;
    }

    public void setCiclista(int ciclista) {
        this.ciclista = ciclista;
    }
}
