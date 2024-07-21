package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BicicletaRepository {
    private final List<Bicicleta> bicicletas = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Bicicleta> findAll() {
        return new ArrayList<>(bicicletas);
    }

    public Optional<Bicicleta> findById(Long id) {
        return bicicletas.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public Bicicleta save(Bicicleta bicicleta) {
        if (bicicleta.getId() == null) {
            bicicleta.setId(counter.incrementAndGet());
        } else {
            bicicletas.removeIf(b -> b.getId().equals(bicicleta.getId()));
        }
        bicicletas.add(bicicleta);
        return bicicleta;
    }

    public void deleteById(Long id) {
        bicicletas.removeIf(b -> b.getId().equals(id));
    }

    public List<Bicicleta> findByTrancaId(Long idTranca) {
        return bicicletas.stream().filter(b -> b.getTranca().getId().equals(idTranca)).toList();
    }
}
