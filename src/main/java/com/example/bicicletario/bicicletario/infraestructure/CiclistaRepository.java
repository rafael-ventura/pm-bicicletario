package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiclistaRepository extends JpaRepository<Ciclista, Long> {
    Optional<Ciclista> findById(int id);
    boolean existsByEmail(String email);
}