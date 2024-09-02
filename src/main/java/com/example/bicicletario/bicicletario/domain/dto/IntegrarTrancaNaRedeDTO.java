package com.example.bicicletario.bicicletario.domain.dto;

/*
* DTO para integrar bicicleta na rede
* */

public class IntegrarTrancaNaRedeDTO {
    private Integer idTotem;
    private Integer idTranca;
    private Integer idFuncionario;

    public Integer getIdTranca() {
        return idTranca;
    }

    public void setIdTranca(Integer idTranca) {
        this.idTranca = idTranca;
    }

    public Integer getIdTotem() {
        return idTotem;
    }

    public void setIdTotem(Integer idTotem) {
        this.idTotem = idTotem;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
