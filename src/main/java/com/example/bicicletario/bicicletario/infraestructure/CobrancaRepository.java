package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CobrancaRepository {

    private Map<Integer, Cobranca> cobrancas = new HashMap<>();
    private int currentId = 1;

    public Cobranca save(Cobranca cobranca) {
        if (cobranca.getId() == 0) {
            cobranca.setId(currentId++);
        }
        cobrancas.put(cobranca.getId(), cobranca);
        return cobranca;
    }

    public Cobranca findById(int id) {
        return cobrancas.get(id);
    }

    public void deleteById(int id) {
        cobrancas.remove(id);
    }

    public Iterable<Cobranca> findAll() {
        return cobrancas.values();
    }
}
