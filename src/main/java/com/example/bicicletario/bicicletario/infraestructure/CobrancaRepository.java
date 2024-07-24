package com.example.bicicletario.bicicletario.infraestructure;
import java.util.*;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import org.springframework.stereotype.Repository;

@Repository
public class CobrancaRepository {

    private final Map<Integer, Cobranca> cobrancas = new HashMap<>();
    private int currentId = 1;

    public Cobranca save(Cobranca cobranca) {
        if (cobranca.getId() == 0) {
            cobranca.setId(currentId++);
        }
        cobrancas.put(cobranca.getId(), cobranca);
        return cobranca;
    }

    public Optional<Cobranca> findById(int id) {
        return Optional.ofNullable(cobrancas.get(id));
    }

    public List<Cobranca> findAll() {
        return new ArrayList<>(cobrancas.values());
    }

    public void updateStatus(int id, StatusCobranca status) {
        Cobranca cobranca = cobrancas.get(id);
        if (cobranca != null) {
            cobranca.setStatusCobranca(status);
        }
    }

    public void deleteById(int id) {
        cobrancas.remove(id);
    }
}