package com.example.bicicletario.bicicletario.dto;

/*
* DTO para integrar bicicleta na rede
* */

public class IntegrarTrancaNaRedeDTO {
    private Long idTranca;
    private Long idBicicleta;
    private Long idFuncionario;

    public Long getIdTranca() {
        return idTranca;
    }

    public void setIdTranca(Long idTranca) {
        this.idTranca = idTranca;
    }

    public Long getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(Long idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
