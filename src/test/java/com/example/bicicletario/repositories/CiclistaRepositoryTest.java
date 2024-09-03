package com.example.bicicletario.repositories;

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
        ciclista.setEmail("test@example.com");

        // Act
        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        // Assert
        assertNotNull(savedCiclista.getId());
        Optional<Ciclista> result = ciclistaRepository.findById(savedCiclista.getId());
        assertTrue(result.isPresent());
        assertEquals(ciclista.getEmail(), result.get().getEmail());
    }

    @Test
    void testExistsByEmail_Success() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("test@example.com");
        ciclistaRepository.save(ciclista);

        // Act
        boolean result = ciclistaRepository.existsByEmail("test@example.com");

        // Assert
        assertTrue(result);
    }

    @Test
    void testExistsByEmail_NotFound() {
        // Act
        boolean result = ciclistaRepository.existsByEmail("notfound@example.com");

        // Assert
        assertFalse(result);
    }

    @Test
    void testExistsById_Success() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("test@example.com");
        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        // Act
        boolean result = ciclistaRepository.existsById(savedCiclista.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void testExistsById_NotFound() {
        // Act
        boolean result = ciclistaRepository.existsById(999);

        // Assert
        assertFalse(result);
    }

    @Test
    void testDeleteCiclista() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("test@example.com");
        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        // Act
        ciclistaRepository.delete(savedCiclista);
        Optional<Ciclista> result = ciclistaRepository.findById(savedCiclista.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSave_MultipleCiclistas() {
        // Arrange
        Ciclista ciclista1 = new Ciclista();
        ciclista1.setEmail("ciclista1@example.com");

        Ciclista ciclista2 = new Ciclista();
        ciclista2.setEmail("ciclista2@example.com");

        // Act
        Ciclista savedCiclista1 = ciclistaRepository.save(ciclista1);
        Ciclista savedCiclista2 = ciclistaRepository.save(ciclista2);

        // Assert
        assertNotEquals(savedCiclista1.getId(), savedCiclista2.getId()); // IDs devem ser diferentes
        assertTrue(ciclistaRepository.existsByEmail("ciclista1@example.com"));
        assertTrue(ciclistaRepository.existsByEmail("ciclista2@example.com"));
    }

    @Test
    void testDelete_NonExistingCiclista() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setId(999); // ID que não existe no repositório

        // Act & Assert
        assertDoesNotThrow(() -> ciclistaRepository.delete(ciclista)); // Deletar um ciclista que não existe não deve lançar exceção
    }
}
