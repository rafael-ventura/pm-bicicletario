package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoDeCreditoRepository extends JpaRepository<CartaoDeCredito, Long> {
    Optional<CartaoDeCredito> findByCiclistaId(Long idCiclista);
}
