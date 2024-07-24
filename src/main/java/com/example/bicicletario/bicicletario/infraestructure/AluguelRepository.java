package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AluguelRepository {
    private final Map<Integer, Aluguel> alugueis = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // Gerador de ID

    public Optional<Aluguel> findByCiclistaAndHoraFimIsNull(int idCiclista) {
        return alugueis.values().stream()
                .filter(aluguel -> aluguel.getCiclista() == idCiclista &&
                        aluguel.getHoraFim() == null)
                .findFirst();
    }

    public boolean existsByCiclistaAndHoraFimIsNull(int ciclistaId) {
        return alugueis.values().stream()
                .anyMatch(aluguel -> aluguel.getCiclista() == ciclistaId &&
                        aluguel.getHoraFim() == null);
    }

    public Aluguel save(Aluguel aluguel) {
        if (aluguel.getId() == 0) {
            aluguel.setId(idGenerator.incrementAndGet()); // Atribui novo ID se não existir
        }
        alugueis.put(aluguel.getId(), aluguel);
        return aluguel;
    }

    public void delete(Aluguel aluguel) {
        alugueis.remove(aluguel.getId());
    }
}
