package com.example.bicicletario.bicicletario.domain;

import jakarta.persistence.*;

@Entity
@Table
public class Tranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(nullable = false, unique = true)
    private int numero;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private String anoDeFabricacao; //esta como string no swagger, mas TODO verificar!

    @Column(nullable = false)
    private String modelo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
