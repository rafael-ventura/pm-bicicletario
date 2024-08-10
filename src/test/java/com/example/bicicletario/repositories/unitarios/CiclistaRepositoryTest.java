package com.example.bicicletario.repositories.unitarios;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class CiclistaRepositoryTest {

    private CiclistaRepository ciclistaRepository;

    @BeforeEach
    void setUp() {
        ciclistaRepository = new CiclistaRepository();
    }

    @Test
    void testSaveAndFindById() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("joao@example.com");

        // Act
        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        // Assert
        assertNotNull(savedCiclista.getId());
        Optional<Ciclista> retrievedCiclista = ciclistaRepository.findById(savedCiclista.getId());
        assertTrue(retrievedCiclista.isPresent());
        assertEquals("joao@example.com", retrievedCiclista.get().getEmail());
    }

    @Test
    void testExistsByEmail_Success() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("joao@example.com");
        ciclistaRepository.save(ciclista);

        // Act
        boolean exists = ciclistaRepository.existsByEmail("joao@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_NotFound() {
        // Act
        boolean exists = ciclistaRepository.existsByEmail("notfound@example.com");

        // Assert
        assertFalse(exists);
    }

    @Test
    void testExistsById_Success() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("joao@example.com");
        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        // Act
        boolean exists = ciclistaRepository.existsById(savedCiclista.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsById_NotFound() {
        // Act
        boolean exists = ciclistaRepository.existsById(999);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testDeleteCiclista() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("joao@example.com");
        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        // Act
        ciclistaRepository.delete(savedCiclista);
        Optional<Ciclista> retrievedCiclista = ciclistaRepository.findById(savedCiclista.getId());

        // Assert
        assertFalse(retrievedCiclista.isPresent());
    }
}
