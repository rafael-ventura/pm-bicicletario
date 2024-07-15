package com.example.bicicletario.bicicletario.domain.dto;

public class AluguelDTO extends NovoAluguelDTO {
    private Integer bicicleta;
    private String horaInicio;
    private Integer trancaFim;
    private String horaFim;
    private Integer cobranca;
    private Integer ciclista;

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

    public Integer getTrancaFim() {
        return trancaFim;
    }

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
