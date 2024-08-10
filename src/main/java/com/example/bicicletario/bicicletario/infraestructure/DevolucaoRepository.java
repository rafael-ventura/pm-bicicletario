package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Devolucao;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DevolucaoRepository {
    private final Map<Integer, Devolucao> devolucoes = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // Gerador de ID

    public Optional<Devolucao> save(Devolucao devolucao) {
        if (devolucao.getId() == 0) {
            devolucao.setId(idGenerator.incrementAndGet()); // Atribui novo ID se não existir
        }
        devolucoes.put(devolucao.getId(), devolucao);
        return Optional.of(devolucao);
    }



}
