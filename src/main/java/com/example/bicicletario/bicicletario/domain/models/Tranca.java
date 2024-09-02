package com.example.bicicletario.bicicletario.domain.models;

import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;

public class Tranca {

    private Integer id;

    private Bicicleta bicicleta;

    private Integer numero;

    private String localizacao;

    private String anoDeFabricacao;

    private String modelo;

    private StatusTranca status;

    private String dataInsercaoTotem;

    private String dataRemocaoTotem;

    private Integer idFuncionarioUltimaOperacao;

    public Tranca() {
    }

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusTranca getStatus() {
        return status;
    }

    public void setStatus(StatusTranca status) {
        this.status = status;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getAnoDeFabricacao() {
        return anoDeFabricacao;
    }

    public void setAnoDeFabricacao(String anoDeFabricacao) {
        this.anoDeFabricacao = anoDeFabricacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
    }

    public String getDataInsercaoTotem() {
        return dataInsercaoTotem;
    }

    public void setDataInsercaoTotem(String dataInsercaoTotem) {
        this.dataInsercaoTotem = dataInsercaoTotem;
    }

    public Integer getIdFuncionarioUltimaOperacao() {
        return idFuncionarioUltimaOperacao;
    }

    public void setIdFuncionarioUltimaOperacao(Integer idFuncionarioUltimaOperacao) {
        this.idFuncionarioUltimaOperacao = idFuncionarioUltimaOperacao;
    }

    public String getDataRemocaoTotem() {
        return dataRemocaoTotem;
    }

    public void setDataRemocaoTotem(String dataRemocaoTotem) {
        this.dataRemocaoTotem = dataRemocaoTotem;
    }
}
