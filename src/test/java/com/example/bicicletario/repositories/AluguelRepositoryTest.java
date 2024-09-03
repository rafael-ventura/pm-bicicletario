package com.example.bicicletario.repositories;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AluguelRepositoryTest {

    private AluguelRepository aluguelRepository;

    @BeforeEach
    void setUp() {
        aluguelRepository = new AluguelRepository();
    }

    @Test
    void testFindByCiclistaAndHoraFimIsNull() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);

        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclista.getId());
        aluguel.setHoraFim(null);

        aluguelRepository.save(aluguel);

        Optional<Aluguel> result = aluguelRepository.findByCiclistaAndHoraFimIsNull(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testExistsByCiclistaAndHoraFimIsNull() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);

        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclista.getId());
        aluguel.setHoraFim(null);

        aluguelRepository.save(aluguel);

        boolean result = aluguelRepository.existsByCiclistaAndHoraFimIsNull(1);
        assertTrue(result);
    }

    @Test
    void testExistsByCiclistaAndHoraFimIsNull_NoAluguel() {
        boolean result = aluguelRepository.existsByCiclistaAndHoraFimIsNull(1);
        assertFalse(result);
    }
}
