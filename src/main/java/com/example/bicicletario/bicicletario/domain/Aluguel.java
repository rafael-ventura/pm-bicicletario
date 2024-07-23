package com.example.bicicletario.bicicletario.domain;

public class Aluguel {

    private int id;
    private int bicicleta;
    private String horaInicio;
    private Integer trancaFim;
    private String horaFim;
    private Integer cobranca;
    private int ciclista;
    private Integer trancaInicio;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(int bicicleta) {
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

    public int getCiclista() {
        return ciclista;
    }

    public void setCiclista(int ciclista) {
        this.ciclista = ciclista;
    }

    public Integer getTrancaInicio() {
        return trancaInicio;
    }

    public void setTrancaInicio(Integer trancaInicio) {
        this.trancaInicio = trancaInicio;
    }
}
