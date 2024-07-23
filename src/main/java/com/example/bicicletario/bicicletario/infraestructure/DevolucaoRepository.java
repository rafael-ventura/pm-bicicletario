/*
package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Aluguel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AluguelRepository {
    private final Map<Integer, Aluguel> alugueis = new HashMap<>();

    public Optional<Aluguel> findByCiclistaAndHoraFimIsNull(int idCiclista) {
        return alugueis.values().stream()
                .filter(aluguel -> aluguel.getCiclista().getId() == idCiclista &&
                        aluguel.getHoraFim() == null)
                .findFirst();
    }

    public boolean existsByCiclistaAndHoraFimIsNull(int ciclistaId) {
        return alugueis.values().stream()
                .anyMatch(aluguel -> aluguel.getCiclista().getId() == ciclistaId &&
                        aluguel.getHoraFim() == null);
    }

}*/
