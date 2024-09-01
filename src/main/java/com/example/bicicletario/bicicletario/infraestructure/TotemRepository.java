package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TotemRepository {
    private final List<Totem> totems = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Totem> findAll() {
        return new ArrayList<>(totems);
    }

    public Optional<Totem> findById(Long id) {
        return totems.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Totem save(Totem totem) {
        if (totem.getId() == null) {
            totem.setId(counter.incrementAndGet());
            totems.add(totem);
            return totem;
        }

        for (int i = 0; i < totems.size(); i++) {
            if (totems.get(i).getId().equals(totem.getId())) {
                totems.set(i, totem);
            }
        }
        return totem;
    }

    //get
    public Totem get(Long id) {
        return totems.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public void deleteById(Long id) {
        totems.removeIf(t -> t.getId().equals(id));
    }

    public boolean existsById(Long idTotem) {
        return totems.stream().anyMatch(t -> t.getId().equals(idTotem));
    }
}
