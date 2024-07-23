package com.example.bicicletario.bicicletario.domain.dto;

public class NovoCiclistaRequestDTO {
    private NovoCiclistaDTO ciclista;
    private NovoCartaoDeCreditoDTO meioDePagamento;

    public NovoCiclistaDTO getCiclista() {
        return ciclista;
    }

    public void setCiclista(NovoCiclistaDTO ciclista) {
        this.ciclista = ciclista;
    }

    public NovoCartaoDeCreditoDTO getMeioDePagamento() {
        return meioDePagamento;
    }

    public void setMeioDePagamento(NovoCartaoDeCreditoDTO meioDePagamento) {
        this.meioDePagamento = meioDePagamento;
    }
}
