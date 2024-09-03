package com.example.bicicletario.bicicletario.domain;

public class Cobranca {
    private int id;
    private String status;
    private String horaSolicitacao;
    private String horaFinalizacao;
    private Double valor;
    private int ciclista;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getCiclista() {
        return ciclista;
    }

    public void setCiclista(int ciclista) {
        this.ciclista = ciclista;
    }
}
