package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrancaRepository extends JpaRepository<Tranca, Long> {
    List<Tranca> findByTotemId(Long idTotem);

    boolean existsByTotemId(Long idTotem);
}
