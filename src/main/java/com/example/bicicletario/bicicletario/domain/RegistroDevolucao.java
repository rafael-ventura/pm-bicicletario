package com.example.bicicletario.bicicletario.domain;

import java.time.LocalDateTime;

public class RegistroDevolucao {

    private int id;
    private int ciclistaId;
    private int bicicletaId;
    private int trancaId;
    private LocalDateTime dataHoraDevolucao;
    private LocalDateTime dataHoraCobranca;
    private Double valorExtra;
    private int cartaoUsado;
    private String statusPagamento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCiclistaId() {
        return ciclistaId;
    }

    public void setCiclistaId(int ciclistaId) {
        this.ciclistaId = ciclistaId;
    }

    public int getBicicletaId() {
        return bicicletaId;
    }

    public void setBicicletaId(int bicicletaId) {
        this.bicicletaId = bicicletaId;
    }

    public int getTrancaId() {
        return trancaId;
    }

    public void setTrancaId(int trancaId) {
        this.trancaId = trancaId;
    }

    public LocalDateTime getDataHoraDevolucao() {
        return dataHoraDevolucao;
    }

    public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) {
        this.dataHoraDevolucao = dataHoraDevolucao;
    }

    public LocalDateTime getDataHoraCobranca() {
        return dataHoraCobranca;
    }

    public void setDataHoraCobranca(LocalDateTime dataHoraCobranca) {
        this.dataHoraCobranca = dataHoraCobranca;
    }

    public Double getValorExtra() {
        return valorExtra;
    }

    public void setValorExtra(Double valorExtra) {
        this.valorExtra = valorExtra;
    }

    public int getCartaoUsado() {
        return cartaoUsado;
    }

    public void setCartaoUsado(int cartaoUsado) {
        this.cartaoUsado = cartaoUsado;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}
