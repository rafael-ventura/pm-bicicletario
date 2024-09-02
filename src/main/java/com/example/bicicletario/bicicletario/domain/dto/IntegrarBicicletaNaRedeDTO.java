package com.example.bicicletario.bicicletario.domain.dto;

/*
* DTO para integrar bicicleta na rede
* */

public class IntegrarBicicletaNaRedeDTO {
    private Integer idTranca;
    private Integer idBicicleta;
    private Integer idFuncionario;

    public Integer getIdTranca() {
        return idTranca;
    }

    public void setIdTranca(Integer idTranca) {
        this.idTranca = idTranca;
    }

    public Integer getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(Integer idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
