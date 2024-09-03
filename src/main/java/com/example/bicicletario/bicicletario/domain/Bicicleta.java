package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;

public class Bicicleta {

    private Integer id;
    private String marca;
    private String modelo;
    private String ano;
    private int numero;
    private StatusBicicleta status;
//    private String dataInsercaoTranca;
//    private String dataRemocaoTranca;
//    private Tranca tranca;
//    private Long idFuncionarioUltimaOperacao;

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public StatusBicicleta getStatus() {
        return status;
    }

    public void setStatus(StatusBicicleta status) {
        this.status = status;
    }
}
