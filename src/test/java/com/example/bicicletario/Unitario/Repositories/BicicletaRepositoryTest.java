package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
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
    void testExistsById() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(null);

        Bicicleta savedBicicleta = bicicletaRepository.save(bicicleta);

        boolean exists = bicicletaRepository.existsById(savedBicicleta.getId());
        assertTrue(exists);
    }

    @Test
    void testExistsById_NotFound() {
        boolean exists = bicicletaRepository.existsById(999);
        assertFalse(exists);
    }
    @Test
    void testFindAll() {
        Bicicleta bicicleta1 = new Bicicleta();
        Bicicleta bicicleta2 = new Bicicleta();

        bicicletaRepository.save(bicicleta1);
        bicicletaRepository.save(bicicleta2);

        List<Bicicleta> bicicletas = bicicletaRepository.findAll();
        assertEquals(2, bicicletas.size());
    }

    @Test
    void testFindAll_Empty() {
        List<Bicicleta> bicicletas = bicicletaRepository.findAll();
        assertTrue(bicicletas.isEmpty());
    }
    @Test
    void testSave_Update() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(null);
        bicicleta.setModelo("Modelo Original");

        Bicicleta savedBicicleta = bicicletaRepository.save(bicicleta);

        // Update the bicicleta's model
        savedBicicleta.setModelo("Modelo Atualizado");
        Bicicleta updatedBicicleta = bicicletaRepository.save(savedBicicleta);

        assertEquals("Modelo Atualizado", updatedBicicleta.getModelo());

        Optional<Bicicleta> retrievedBicicleta = bicicletaRepository.findById(updatedBicicleta.getId());
        assertTrue(retrievedBicicleta.isPresent());
        assertEquals("Modelo Atualizado", retrievedBicicleta.get().getModelo());
    }
    @Test
    void testDeleteById_NotFound() {
        // Try to delete a bicicleta by a non-existent ID
        bicicletaRepository.deleteById(999);

        // Ensure no exceptions were thrown and repository is still consistent
        assertTrue(bicicletaRepository.findAll().isEmpty());
    }

}

