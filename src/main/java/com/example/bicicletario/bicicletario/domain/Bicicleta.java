package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;


public class Bicicleta {

    public int id;
    public String marca;
    public String modelo;
    public String ano;
    public int numero;
    public StatusBicicleta statusBicicleta;
    public String dataInsercaoTranca;

    public Bicicleta() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
