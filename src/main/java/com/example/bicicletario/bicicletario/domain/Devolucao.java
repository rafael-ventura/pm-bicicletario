package com.example.bicicletario.bicicletario.domain;

public class Devolucao {

    private int id;
    private int idAluguel;
    private int idBicicleta;
    private int idTranca;
    private String dataHoraDevolucao;
    private String dataHoraCobranca;
    private Double valorExtra;
    private String cartaoUsado;
    private String statusPagamento; // Ex: "SUCESSO", "FALHA"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAluguel() {
        return idAluguel;
    }

    public void setIdAluguel(int idAluguel) {
        this.idAluguel = idAluguel;
    }

    public int getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(int idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public int getIdTranca() {
        return idTranca;
    }

    public void setIdTranca(int idTranca) {
        this.idTranca = idTranca;
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
