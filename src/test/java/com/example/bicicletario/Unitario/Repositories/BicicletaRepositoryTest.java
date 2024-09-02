package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


}

