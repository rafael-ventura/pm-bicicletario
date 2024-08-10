package com.example.bicicletario.bicicletario.domain.dto;

public class NovoDevolucaoDTO {

    private int aluguelId;
    private int bicicletaId;
    private int trancaId;
    private String dataHoraDevolucao;
    private String dataHoraCobranca;
    private Double valorExtra;
    private String cartaoUsado;
    private String statusPagamento;

    public int getAluguelId() {
        return aluguelId;
    }

    public void setAluguelId(int aluguelId) {
        this.aluguelId = aluguelId;
    }

    public int getIdBicicleta() {
        return bicicletaId;
    }

    public void setIdBicicleta(int bicicletaId) {
        this.bicicletaId = bicicletaId;
    }

    public int getIdTranca() {
        return trancaId;
    }

    public void setIdTranca(int trancaId) {
        this.trancaId = trancaId;
    }

    public String getDataHoraDevolucao() {
        return dataHoraDevolucao;
    }

    public void setDataHoraDevolucao(String dataHoraDevolucao) {
        this.dataHoraDevolucao = dataHoraDevolucao;
    }

    public String getDataHoraCobranca() {
        return dataHoraCobranca;
    }

    public void setDataHoraCobranca(String dataHoraCobranca) {
        this.dataHoraCobranca = dataHoraCobranca;
    }

    public Double getValorExtra() {
        return valorExtra;
    }

    public void setValorExtra(Double valorExtra) {
        this.valorExtra = valorExtra;
    }

    public String getCartaoUsado() {
        return cartaoUsado;
    }

    public void setCartaoUsado(String cartaoUsado) {
        this.cartaoUsado = cartaoUsado;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}
