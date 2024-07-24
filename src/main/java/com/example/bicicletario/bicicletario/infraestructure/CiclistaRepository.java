package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CiclistaRepository {
    private final Map<Integer, Ciclista> ciclistas = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // Gerador de ID

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
        if (ciclista.getId() == null || ciclista.getId() == 0) {
            ciclista.setId(idGenerator.incrementAndGet()); // Atribui novo ID se não existir
        }
        ciclistas.put(ciclista.getId(), ciclista);
        return ciclista;
    }

    public void delete(Ciclista ciclista) {
        ciclistas.remove(ciclista.getId());
    }
}
