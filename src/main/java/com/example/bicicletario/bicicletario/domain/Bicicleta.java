package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;

public class Bicicleta {

    private Long id;
    private String marca;
    private String modelo;
    private String ano;
    private int numero;
    private StatusBicicleta statusBicicleta;
    private String dataInsercaoTranca;
    private String dataRemocaoTranca;
    private Tranca tranca;
    private Long idFuncionarioUltimaOperacao;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public StatusBicicleta getStatusBicicleta() {
        return statusBicicleta;
    }

    public void setStatusBicicleta(StatusBicicleta statusBicicleta) {
        this.statusBicicleta = statusBicicleta;
    }

    public String getDataInsercaoTranca() {
        return dataInsercaoTranca;
    }

    public void setDataInsercaoTranca(String dataInsercaoTranca) {
        this.dataInsercaoTranca = dataInsercaoTranca;
    }

    public String getDataRemocaoTranca() {
        return dataRemocaoTranca;
    }

    public void setDataRemocaoTranca(String dataRemocaoTranca) {
        this.dataRemocaoTranca = dataRemocaoTranca;
    }

    public Tranca getTranca() {
        return tranca;
    }

    public void setTranca(Tranca tranca) {
        this.tranca = tranca;
    }

    public Long getIdFuncionarioUltimaOperacao() {
        return idFuncionarioUltimaOperacao;
    }

    public void setIdFuncionarioUltimaOperacao(Long idFuncionarioUltimaOperacao) {
        this.idFuncionarioUltimaOperacao = idFuncionarioUltimaOperacao;
    }
}
