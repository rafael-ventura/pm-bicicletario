package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
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
        tranca.setLocalizacao("Localizacao");

        trancaRepository.save(tranca);

        Optional<Tranca> foundTranca = trancaRepository.findById(1);
        assertTrue(foundTranca.isPresent());
        assertEquals(1, foundTranca.get().getId());
    }

    @Test
    void testDeleteById() {
        Tranca tranca = new Tranca();
        Tranca savedTranca = trancaRepository.save(tranca);
        trancaRepository.deleteById(1);

        Optional<Tranca> retrievedTranca = trancaRepository.findById(1);
        assertFalse(retrievedTranca.isPresent());
    }

    @Test
    void testFindAll() {
        Tranca tranca1 = new Tranca();
        Tranca tranca2 = new Tranca();

        trancaRepository.save(tranca1);
        trancaRepository.save(tranca2);

        List<Tranca> result = trancaRepository.findAll();
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void testExistsById() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        Tranca tranca = new Tranca();
        tranca.setBicicleta(bicicleta);

        trancaRepository.save(tranca);

        assertTrue(trancaRepository.existsByBicicletaId(1));
    }

    @Test
    void testFindTrancaByLocalizacao() {
        Tranca tranca = new Tranca();
        tranca.setLocalizacao("Localizacao");

        trancaRepository.save(tranca);

        List<Tranca> result = trancaRepository.findTrancaByLocalizacao("Localizacao");
        assertEquals(1, result.size());
        assertEquals("Localizacao", result.get(0).getLocalizacao());
    }


    @Test
    void testExistsById_NotFound() {
        boolean exists = trancaRepository.existsByBicicletaId(999);
        assertFalse(exists);
    }

    @Test
    void testFindAll_Empty() {
        List<Tranca> trancas = trancaRepository.findAll();
        assertTrue(trancas.isEmpty());
    }

    @Test
    void testDeleteById_NotFound() {
        // Try to delete a tranca by a non-existent ID
        trancaRepository.deleteById(999);

        // Ensure no exceptions were thrown and repository is still consistent
        assertTrue(trancaRepository.findAll().isEmpty());
    }

}

