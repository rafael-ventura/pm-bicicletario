package com.example.bicicletario.bicicletario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "aluguel")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bicicleta")
    private Integer bicicleta;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "tranca_fim")
    private Integer trancaFim;

    @Column(name = "hora_fim")
    private String horaFim;

    @Column(name = "cobranca")
    private Integer cobranca;

    @Column(name = "ciclista")
    private Integer ciclista;

    @Column(name = "tranca_inicio")
    private Integer trancaInicio;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Integer getCiclista() {
        return ciclista;
    }

    public void setCiclista(Integer ciclista) {
        this.ciclista = ciclista;
    }

    public Integer getTrancaInicio() {
        return trancaInicio;
    }

    public void setTrancaInicio(Integer trancaInicio) {
        this.trancaInicio = trancaInicio;
    }
}
