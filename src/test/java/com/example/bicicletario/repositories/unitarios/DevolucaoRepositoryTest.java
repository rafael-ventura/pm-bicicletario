package com.example.bicicletario.repositories;

import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.infraestructure.DevolucaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DevolucaoRepositoryTest {

    private DevolucaoRepository devolucaoRepository;

    @BeforeEach
    void setUp() {
        devolucaoRepository = new DevolucaoRepository();
    }

    @Test
    void testSaveDevolucao() {
        // Arrange
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(1);
        devolucao.setIdAluguel(1);

        // Act
        Optional<Devolucao> savedDevolucao = devolucaoRepository.save(devolucao);

        // Assert
        assertTrue(savedDevolucao.isPresent());
        assertEquals(1, savedDevolucao.get().getId());
    }

    @Test
    void testFindByBicicleta_Success() {
        // Arrange
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setIdAluguel(3);

        devolucaoRepository.save(devolucao);

        // Act
        Optional<Devolucao> result = devolucaoRepository.findByBicicleta(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getIdBicicleta());
    }

    @Test
    void testFindByTranca_Success() {
        // Arrange
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setIdAluguel(3);

        devolucaoRepository.save(devolucao);

        // Act
        Optional<Devolucao> result = devolucaoRepository.findByTranca(2);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getIdTranca());
    }

    @Test
    void testFindByAluguel_Success() {
        // Arrange
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setIdAluguel(3);

        devolucaoRepository.save(devolucao);

        // Act
        Optional<Devolucao> result = devolucaoRepository.findByAluguel(3);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(3, result.get().getIdAluguel());
    }

    @Test
    void testExistsByBicicleta_Success() {
        // Arrange
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setIdAluguel(3);

        devolucaoRepository.save(devolucao);

        // Act
        boolean exists = devolucaoRepository.existsByBicicleta(1);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByTranca_Success() {
        // Arrange
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setIdAluguel(3);

        devolucaoRepository.save(devolucao);

        // Act
        boolean exists = devolucaoRepository.existsByTranca(2);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByBicicleta_NoMatch() {
        // Act
        boolean exists = devolucaoRepository.existsByBicicleta(999);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testExistsByTranca_NoMatch() {
        // Act
        boolean exists = devolucaoRepository.existsByTranca(999);

        // Assert
        assertFalse(exists);
    }
}
