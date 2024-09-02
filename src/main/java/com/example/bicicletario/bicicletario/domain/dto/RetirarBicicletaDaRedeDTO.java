package com.example.bicicletario.bicicletario.domain.dto;

/*
* DTO para retirar bicicleta da rede
* */

import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;

public class RetirarBicicletaDaRedeDTO {
    private Integer idTranca;
    private Integer idBicicleta;
    private Integer idFuncionario;
    private StatusAcaoReparador statusAcaoReparador;

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

    public StatusAcaoReparador getStatusAcaoReparador() {
        return statusAcaoReparador;
    }

    public void setStatusAcaoReparador(StatusAcaoReparador statusAcaoReparador) {
        this.statusAcaoReparador = statusAcaoReparador;
    }
}
