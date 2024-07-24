package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;

import java.math.BigDecimal;

public class Cobranca {
    private int id;
    private StatusCobranca statusCobranca;
    private String horaSolicitacao;
    private String horaFinalizacao;
    private BigDecimal valor;
    private int ciclista;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusCobranca getStatusCobranca() {
        return statusCobranca;
    }

    public void setStatusCobranca(StatusCobranca statusCobranca) {
        this.statusCobranca = statusCobranca;
    }

    public String getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public void setHoraSolicitacao(String horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }

    public String getHoraFinalizacao() {
        return horaFinalizacao;
    }

    public void setHoraFinalizacao(String horaFinalizacao) {
        this.horaFinalizacao = horaFinalizacao;
    }

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
