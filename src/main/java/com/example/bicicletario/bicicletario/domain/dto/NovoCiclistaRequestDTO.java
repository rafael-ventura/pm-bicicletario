package com.example.bicicletario.bicicletario.domain.dto;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;

public class NovoCiclistaRequestDTO {
    private NovoCiclistaDTO ciclista;
    private CartaoDeCredito meioDePagamento;

    // Getters e Setters

    public NovoCiclistaDTO getCiclista() {
        return ciclista;
    }

    public void setCiclista(NovoCiclistaDTO ciclista) {
        this.ciclista = ciclista;
    }

    public CartaoDeCredito getMeioDePagamento() {
        return meioDePagamento;
    }

    public void setMeioDePagamento(CartaoDeCredito meioDePagamento) {
        this.meioDePagamento = meioDePagamento;
    }
}
