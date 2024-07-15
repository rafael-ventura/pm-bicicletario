package com.example.bicicletario.bicicletario.domain.dto;

/*
* DTO para retirar bicicleta da rede
* */

import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;

public class RetirarTrancaDaRedeDTO {
    private Long idTotem;
    private Long idTranca;
    private Long idFuncionario;
    private StatusAcaoReparador statusAcaoReparador;

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

    public StatusAcaoReparador getStatusAcaoReparador() {
        return statusAcaoReparador;
    }

    public void setStatusAcaoReparador(StatusAcaoReparador statusAcaoReparador) {
        this.statusAcaoReparador = statusAcaoReparador;
    }
}
