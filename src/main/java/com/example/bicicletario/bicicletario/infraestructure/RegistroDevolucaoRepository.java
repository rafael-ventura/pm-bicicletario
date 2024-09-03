package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.RegistroDevolucao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RegistroDevolucaoRepository {
    private final Map<Integer, RegistroDevolucao> registros = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // Gerador de ID

    public RegistroDevolucao save(RegistroDevolucao registro) {
        if (registro.getId() == 0) {
            registro.setId(idGenerator.incrementAndGet()); // Atribui novo ID se não existir
        }
        registros.put(registro.getId(), registro);
        return registro;
    }
}
