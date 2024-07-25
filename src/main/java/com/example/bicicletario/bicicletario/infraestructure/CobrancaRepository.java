package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CobrancaRepository {

    private static final Logger logger = LoggerFactory.getLogger(CobrancaRepository.class);

    private final Map<Integer, Cobranca> cobrancas = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger();


    public Cobranca save(Cobranca cobranca) {
        if (cobranca.getId() == null) {
            cobranca.setId(counter.incrementAndGet());
        }
        cobrancas.put(cobranca.getId(), cobranca);
        return cobranca;
    }

    public Cobranca findById(int id) {
        logger.info("Retornando cobranca com ID: {}", id);
        return cobrancas.get(id);
    }

    public void deleteById(int id) {
        cobrancas.remove(id);
    }

    public Iterable<Cobranca> findAll() {
        return cobrancas.values();
    }
}
