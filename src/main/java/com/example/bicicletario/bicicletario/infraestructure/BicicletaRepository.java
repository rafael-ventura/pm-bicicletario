package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
}
