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
        if (totem.getId() != null) {
            totems.removeIf(t -> t.getId().equals(totem.getId()));
        }
        totem.setId(counter.incrementAndGet());
        totems.add(totem);
        return totem;
    }

    public void deleteById(Long id) {
        totems.removeIf(t -> t.getId().equals(id));
    }

    public boolean existsById(Long idTotem) {
        return totems.stream().anyMatch(t -> t.getId().equals(idTotem));
    }
}
