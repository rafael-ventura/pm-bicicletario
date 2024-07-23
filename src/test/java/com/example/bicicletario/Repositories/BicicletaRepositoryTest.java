package com.example.bicicletario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BicicletaRepositoryTest {

    private BicicletaRepository bicicletaRepository;

    @BeforeEach
    void setUp() {
        bicicletaRepository = new BicicletaRepository();
    }

    @Test
    void testSaveAndFindById() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(null);

        Bicicleta savedBicicleta = bicicletaRepository.save(bicicleta);

        assertNotNull(savedBicicleta.getId());
        Optional<Bicicleta> retrievedBicicleta = bicicletaRepository.findById(savedBicicleta.getId());
        assertTrue(retrievedBicicleta.isPresent());
        assertEquals(savedBicicleta.getId(), retrievedBicicleta.get().getId());
    }

    @Test
    void testDeleteById() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(null);

        Bicicleta savedBicicleta = bicicletaRepository.save(bicicleta);
        bicicletaRepository.deleteById(savedBicicleta.getId());

        Optional<Bicicleta> retrievedBicicleta = bicicletaRepository.findById(savedBicicleta.getId());
        assertFalse(retrievedBicicleta.isPresent());
    }

    @Test
    void testFindByTrancaId() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        Bicicleta bicicleta1 = new Bicicleta();
        bicicleta1.setTranca(tranca);
        bicicletaRepository.save(bicicleta1);

        Bicicleta bicicleta2 = new Bicicleta();
        bicicleta2.setTranca(tranca);
        bicicletaRepository.save(bicicleta2);

        List<Bicicleta> bicicletas = bicicletaRepository.findByTrancaId(1L);
        assertEquals(2, bicicletas.size());
    }

    @Test
    void testFindByTotemId() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.getTotem().setId(1L);

        Bicicleta bicicleta1 = new Bicicleta();
        bicicleta1.setTranca(tranca);
        bicicletaRepository.save(bicicleta1);

        Bicicleta bicicleta2 = new Bicicleta();
        bicicleta2.setTranca(tranca);
        bicicletaRepository.save(bicicleta2);

        List<Bicicleta> bicicletas = bicicletaRepository.findByTotemId(1L);
        assertEquals(2, bicicletas.size());
    }
}
