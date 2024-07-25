package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrancaRepository {
    private final List<Tranca> trancas = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Tranca> findAll() {
        return new ArrayList<>(trancas);
    }

    public Optional<Tranca> findById(Long id) {
        return trancas.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Tranca save(Tranca tranca) {
        if (tranca.getId() != null) {
            trancas.removeIf(t -> t.getId().equals(tranca.getId()));
        }
        tranca.setId(counter.incrementAndGet());
        trancas.add(tranca);
        return tranca;
    }

    public void deleteById(Long id) {
        trancas.removeIf(t -> t.getId().equals(id));
    }

    public List<Tranca> findByTotemId(Long idTotem) {
        List<Tranca> result = new ArrayList<>();
        for (Tranca tranca : trancas) {
            if (tranca.getTotem().getId().equals(idTotem)) {
                result.add(tranca);
            }
        }
        return result;
    }

    public boolean existsByTotemId(Long idTotem) {
        return trancas.stream().anyMatch(t -> t.getTotem().getId().equals(idTotem));
    }
}
