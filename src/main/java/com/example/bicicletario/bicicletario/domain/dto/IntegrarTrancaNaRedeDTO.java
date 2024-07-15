package com.example.bicicletario.bicicletario.domain.dto;

/*
* DTO para integrar bicicleta na rede
* */

public class IntegrarTrancaNaRedeDTO {
    private Long idTotem;
    private Long idTranca;
    private Long idFuncionario;

    public Long getIdTranca() {
        return idTranca;
    }

    public void setIdTranca(Long idTranca) {
        this.idTranca = idTranca;
    }

    public Long getIdTotem() {
        return idTotem;
    }

    public void setIdTotem(Long idTotem) {
        this.idTotem = idTotem;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
