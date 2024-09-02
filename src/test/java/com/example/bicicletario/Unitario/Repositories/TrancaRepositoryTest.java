/*
package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrancaRepositoryTest {

    private TrancaRepository trancaRepository;

    @BeforeEach
    void setUp() {
        trancaRepository = new TrancaRepository();
    }

    @Test
    void testSaveAndFindById() {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        Tranca savedTranca = trancaRepository.save(tranca);

        assertEquals(1, savedTranca.getId());
        Optional<Tranca> retrievedTranca = trancaRepository.findById(1);
        assertTrue(retrievedTranca.isPresent());
        assertEquals(1, retrievedTranca.get().getId());
    }

    @Test
    void testDeleteById() {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        trancaRepository.save(tranca);
        trancaRepository.deleteById(1);

        Optional<Tranca> retrievedTranca = trancaRepository.findById(1);
        assertFalse(retrievedTranca.isPresent());
    }

    @Test
    void testFindByTotemId() {
        Totem totem = new Totem();
        totem.setId(1);

        Tranca tranca1 = new Tranca();
        tranca1.setId(1);
        tranca1.setTotem(totem);
        trancaRepository.save(tranca1);

        Tranca tranca2 = new Tranca();
        tranca2.setId(2L);
        tranca2.setTotem(totem);
        trancaRepository.save(tranca2);

        List<Tranca> trancas = trancaRepository.findByTotemId(1);
        assertEquals(2, trancas.size());
    }

    @Test
    void testExistsByTotemId() {
        Totem totem = new Totem();
        totem.setId(1);

        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setTotem(totem);
        trancaRepository.save(tranca);

        assertTrue(trancaRepository.existsByTotemId(1));
    }
}
*/
