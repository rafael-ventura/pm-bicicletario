package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    List<Bicicleta> findByTotemId(Long idTotem);
    List<Bicicleta> findByStatus(String status);
}
