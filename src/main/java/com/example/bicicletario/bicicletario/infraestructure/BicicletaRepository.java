package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class BicicletaRepository {
    private final List<Bicicleta> bicicletas = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    public List<Bicicleta> findAll() {
        return new ArrayList<>(bicicletas);
    }

    public Optional<Bicicleta> findById(Integer id) {
        return bicicletas.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
    }

    public Bicicleta save(Bicicleta bicicleta) {
        if (bicicleta.getId() == null) {
            bicicleta.setId(counter.incrementAndGet());
            bicicletas.add(bicicleta);
            return bicicleta;
        }

        for (int i = 0; i < bicicletas.size(); i++) {
            if (Objects.equals(bicicletas.get(i).getId(), bicicleta.getId())) {
                bicicletas.set(i, bicicleta);
            }
        }

        return bicicleta;
    }

    public void deleteById(Integer id) {
        bicicletas.removeIf(b -> Objects.equals(b.getId(), id));
    }

    public boolean existsById(Integer idBicicleta) {
        return bicicletas.stream().anyMatch(b -> Objects.equals(b.getId(), idBicicleta));
    }


}
