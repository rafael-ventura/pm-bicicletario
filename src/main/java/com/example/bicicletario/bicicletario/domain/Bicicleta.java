package com.example.bicicletario.bicicletario.domain;

import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import jakarta.persistence.*;

@Entity
@Table(name = "bicicletas")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "marca")
    public String marca;

    @Column(name = "modelo")
    public String modelo;

    @Column(name = "ano")
    public String ano;

    @Column(name = "numero")
    public int numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public StatusBicicleta statusBicicleta;

    //dataInsercaoTranca
    @Column(name = "data_insercao_tranca")
    public String dataInsercaoTranca;

    @ManyToOne
    @JoinColumn(name = "tranca_id")
    private Tranca tranca;

    public Bicicleta(String marca, String modelo, String ano, int numero, StatusBicicleta statusBicicleta) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.numero = numero;
        this.statusBicicleta = statusBicicleta;
    }

    public Bicicleta() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public StatusBicicleta getStatus() {
        return statusBicicleta;
    }

    public void setStatus(StatusBicicleta statusBicicleta) {
        this.statusBicicleta = statusBicicleta;
    }

    public String getDataInsercaoTranca() {
        return dataInsercaoTranca;
    }

    public void setDataInsercaoTranca(String dataInsercaoTranca) {
        this.dataInsercaoTranca = dataInsercaoTranca;
    }

    public Tranca getTranca() {
        return tranca;
    }

    public void setTranca(Tranca tranca) {
        this.tranca = tranca;
    }
}
