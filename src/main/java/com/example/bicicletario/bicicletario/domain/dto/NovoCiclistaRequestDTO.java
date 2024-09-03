package com.example.bicicletario.bicicletario.domain.dto;

import com.example.bicicletario.bicicletario.domain.Ciclista;

public class NovoCiclistaRequestDTO {
    private Ciclista ciclista;
    private NovoCartaoDeCreditoDTO meioDePagamento;

    public Ciclista getCiclista() {
        return ciclista;
    }

    public void setCiclista(Ciclista ciclista) {
        this.ciclista = ciclista;
    }

    public NovoCartaoDeCreditoDTO getMeioDePagamento() {
        return meioDePagamento;
    }

    public void setMeioDePagamento(NovoCartaoDeCreditoDTO meioDePagamento) {
        this.meioDePagamento = meioDePagamento;
    }
}
