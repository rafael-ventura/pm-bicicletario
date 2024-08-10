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
        tranca.setId(1L);

        Tranca savedTranca = trancaRepository.save(tranca);

        assertEquals(1L, savedTranca.getId());
        Optional<Tranca> retrievedTranca = trancaRepository.findById(1L);
        assertTrue(retrievedTranca.isPresent());
        assertEquals(1L, retrievedTranca.get().getId());
    }

    @Test
    void testDeleteById() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        trancaRepository.save(tranca);
        trancaRepository.deleteById(1L);

        Optional<Tranca> retrievedTranca = trancaRepository.findById(1L);
        assertFalse(retrievedTranca.isPresent());
    }

    @Test
    void testFindByTotemId() {
        Totem totem = new Totem();
        totem.setId(1L);

        Tranca tranca1 = new Tranca();
        tranca1.setId(1L);
        tranca1.setTotem(totem);
        trancaRepository.save(tranca1);

        Tranca tranca2 = new Tranca();
        tranca2.setId(2L);
        tranca2.setTotem(totem);
        trancaRepository.save(tranca2);

        List<Tranca> trancas = trancaRepository.findByTotemId(1L);
        assertEquals(2, trancas.size());
    }

    @Test
    void testExistsByTotemId() {
        Totem totem = new Totem();
        totem.setId(1L);

        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setTotem(totem);
        trancaRepository.save(tranca);

        assertTrue(trancaRepository.existsByTotemId(1L));
    }
}
