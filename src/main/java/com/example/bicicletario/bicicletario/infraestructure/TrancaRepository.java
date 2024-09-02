package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TrancaRepository {
    private final List<Tranca> trancas = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    public List<Tranca> findAll() {
        return new ArrayList<>(trancas);
    }

    public Optional<Tranca> findById(Integer id) {
        return trancas.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Tranca save(Tranca tranca) {
        if (tranca.getId() == null) {
            tranca.setId(counter.incrementAndGet());
            trancas.add(tranca);
            return tranca;
        }

        for (int i = 0; i < trancas.size(); i++) {
            if (trancas.get(i).getId().equals(tranca.getId())) {
                trancas.set(i, tranca);
            }
        }
        return tranca;
    }

    public void deleteById(Integer id) {
        trancas.removeIf(t -> t.getId().equals(id));
    }

    public List<Tranca> findTrancaByLocalizacao(String totemLocalizacao) {
        List<Tranca> result = new ArrayList<>();
        for (Tranca tranca : trancas) {
            if (tranca.getLocalizacao().equals(totemLocalizacao)) {
                result.add(tranca);
            }
        }
        return result;
    }

    public boolean existsByBicicletaId(Integer id) {
        return trancas.stream().anyMatch(t -> t.getBicicleta().getId().equals(id));
    }
}
