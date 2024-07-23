package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CiclistaRepository {
    private final Map<Integer, Ciclista> ciclistas = new HashMap<>();

    public Optional<Ciclista> findById(int id) {
        return Optional.ofNullable(ciclistas.get(id));
    }

    public boolean existsByEmail(String email) {
        return ciclistas.values().stream()
                .anyMatch(ciclista -> ciclista.getEmail().equals(email));
    }

    public boolean existsById(int id) {
        return ciclistas.containsKey(id);
    }

    public Ciclista save(Ciclista ciclista) {
        ciclistas.put(ciclista.getId(), ciclista);
        return ciclista;
    }

    public void delete(Ciclista ciclista) {
        ciclistas.remove(ciclista.getId());
    }
}