package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TotemRepository {
    private final List<Totem> totems = new ArrayList<>();

    public List<Totem> findAll() {
        return new ArrayList<>(totems);
    }

    public Optional<Totem> findById(Long id) {
        return totems.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Totem save(Totem totem) {
        totems.removeIf(t -> t.getId().equals(totem.getId()));
        totems.add(totem);
        return totem;
    }

    public void deleteById(Long id) {
        totems.removeIf(t -> t.getId().equals(id));
    }

}
