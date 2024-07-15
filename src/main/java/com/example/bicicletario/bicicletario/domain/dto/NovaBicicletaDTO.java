package com.example.bicicletario.bicicletario.domain.dto;

import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;

public class NovaBicicletaDTO {
    private String marca;
    private String modelo;
    private String ano;
    private int numero;
    private StatusBicicleta status;

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

    public StatusBicicleta getStatus() {
        return status;
    }

    public void setStatus(StatusBicicleta status) {
        this.status = status;
    }
}

