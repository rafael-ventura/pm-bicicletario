package com.example.bicicletario.bicicletario.domain.models;

import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;

public class Tranca {

    private Long id;

    private Bicicleta bicicleta;

    private int numero;

    private String localizacao;

    private String anoDeFabricacao;

    private String modelo;

    private StatusTranca status;

    private String dataInsercaoTotem;

    private String dataRemocaoTotem;

    private Long idFuncionarioUltimaOperacao;

    private Totem totem; //TODO - REVISAR

    public Tranca() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusTranca getStatus() {
        return status;
    }

    public void setStatus(StatusTranca status) {
        this.status = status;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
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

    public Totem getTotem() {
        return totem;
    }

    public void setTotem(Totem totem) {
        this.totem = totem;
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

    public Long getIdFuncionarioUltimaOperacao() {
        return idFuncionarioUltimaOperacao;
    }

    public void setIdFuncionarioUltimaOperacao(Long idFuncionarioUltimaOperacao) {
        this.idFuncionarioUltimaOperacao = idFuncionarioUltimaOperacao;
    }

    public String getDataRemocaoTotem() {
        return dataRemocaoTotem;
    }

    public void setDataRemocaoTotem(String dataRemocaoTotem) {
        this.dataRemocaoTotem = dataRemocaoTotem;
    }
}
