package com.example.bicicletario.bicicletario.domain.models;

import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;

public class Bicicleta {

    public Long id;

    public String marca;

    public String modelo;

    public String ano;

    public int numero;

    public StatusBicicleta statusBicicleta;

    public String dataInsercaoTranca;

    private String dataRemocaoTranca;

    private Tranca tranca;

    public Bicicleta() {

    }

    public Bicicleta(String marca, String modelo, String ano, int numero, StatusBicicleta statusBicicleta) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.numero = numero;
        this.statusBicicleta = statusBicicleta;
    }

    public String getDataInsercaoTranca() {
        return dataInsercaoTranca;
    }

    public void setDataInsercaoTranca(String dataInsercaoTranca) {
        this.dataInsercaoTranca = dataInsercaoTranca;
    }

    public Tranca getTranca() {
        return tranca;
    }

    public void setTranca(Tranca tranca) {
        this.tranca = tranca;
    }

    public StatusBicicleta getStatusBicicleta() {
        return statusBicicleta;
    }

    public void setStatusBicicleta(StatusBicicleta statusBicicleta) {
        this.statusBicicleta = statusBicicleta;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataRemocaoTranca() {
        return dataRemocaoTranca;
    }

    public void setDataRemocaoTranca(String string) {
        this.dataRemocaoTranca = string;
    }
}
