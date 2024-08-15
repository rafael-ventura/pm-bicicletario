package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Devolucao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class DevolucaoRepository {
    private final Map<Integer, Devolucao> devolucoes = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    // Salva ou atualiza uma devolução
    public Optional<Devolucao> save(Devolucao devolucao) {
        if (devolucao.getId() == 0) {
            devolucao.setId(idGenerator.incrementAndGet());
        }
        devolucoes.put(devolucao.getId(), devolucao);
        return Optional.of(devolucao);
    }

    // Encontra uma devolução por ID de bicicleta
    public Optional<Devolucao> findByBicicleta(int idBicicleta) {
        return devolucoes.values().stream()
                .filter(devolucao -> devolucao.getIdBicicleta() == idBicicleta)
                .findFirst();
    }

    // Encontra uma devolução por ID de tranca
    public Optional<Devolucao> findByTranca(int idTranca) {
        return devolucoes.values().stream()
                .filter(devolucao -> devolucao.getIdTranca() == idTranca)
                .findFirst();
    }

    // Encontra uma devolução por ID de aluguel
    public Optional<Devolucao> findByAluguel(int idAluguel) {
        return devolucoes.values().stream()
                .filter(devolucao -> devolucao.getIdAluguel() == idAluguel)
                .findFirst();
    }

    // Verifica se existe uma devolução para uma determinada bicicleta
    public boolean existsByBicicleta(int idBicicleta) {
        return devolucoes.values().stream()
                .anyMatch(devolucao -> devolucao.getIdBicicleta() == idBicicleta);
    }

    // Verifica se existe uma devolução para uma determinada tranca
    public boolean existsByTranca(int idTranca) {
        return devolucoes.values().stream()
                .anyMatch(devolucao -> devolucao.getIdTranca() == idTranca);
    }
}
