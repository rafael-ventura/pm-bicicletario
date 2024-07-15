package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    Optional<Aluguel> findByCiclistaIdAndBicicletaIdAndDataHoraFimIsNull(Long idCiclista, Long idBicicleta);

    boolean existsByCiclistaAndHoraFimIsNull(int ciclistaId);
}
