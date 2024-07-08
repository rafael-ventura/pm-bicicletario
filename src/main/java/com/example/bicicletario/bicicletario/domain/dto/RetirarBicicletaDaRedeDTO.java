package com.example.bicicletario.bicicletario.domain.dto;

/*
* DTO para retirar bicicleta da rede
* */

import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;

public class RetirarBicicletaDaRedeDTO {
    private Long idTranca;
    private Long idBicicleta;
    private Long idFuncionario;
    private StatusAcaoReparador statusAcaoReparador;

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

    public StatusAcaoReparador getStatusAcaoReparador() {
        return statusAcaoReparador;
    }

    public void setStatusAcaoReparador(StatusAcaoReparador statusAcaoReparador) {
        this.statusAcaoReparador = statusAcaoReparador;
    }
}
