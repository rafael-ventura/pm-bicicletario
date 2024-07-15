package com.example.bicicletario.bicicletario.domain.dto;

public class DevolucaoDTO extends NovoDevolucaoDTO {
    private Integer bicicleta;
    private String horaInicio;
    private Integer trancaFim; // This field is inherited from NovoDevolucao
    private String horaFim;
    private Integer cobranca;

    // Getters and Setters
    public Integer getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Integer bicicleta) {
        this.bicicleta = bicicleta;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    @Override
    public Integer getTrancaFim() {
        return trancaFim;
    }

    @Override
    public void setTrancaFim(Integer trancaFim) {
        this.trancaFim = trancaFim;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public Integer getCobranca() {
        return cobranca;
    }

    public void setCobranca(Integer cobranca) {
        this.cobranca = cobranca;
    }
}
