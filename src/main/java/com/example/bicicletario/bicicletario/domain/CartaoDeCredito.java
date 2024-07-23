package com.example.bicicletario.bicicletario.domain;

public class CartaoDeCredito {

    public int id;

    public String nomeTitular;

    public String numero;

    public String validade;

    public String cvv;

    public int idCiclista;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getIdCiclista() {
        return idCiclista;
    }

    public void setIdCiclista(int idCiclista) {
        this.idCiclista = idCiclista;
    }
}
