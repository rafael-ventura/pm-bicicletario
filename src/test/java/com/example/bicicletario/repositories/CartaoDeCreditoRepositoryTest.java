package com.example.bicicletario.repositories;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.infraestructure.CartaoDeCreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartaoDeCreditoRepositoryTest {

    private CartaoDeCreditoRepository cartaoDeCreditoRepository;

    @BeforeEach
    void setUp() {
        cartaoDeCreditoRepository = new CartaoDeCreditoRepository();
    }

    @Test
    void testSaveAndFindById() {
        // Arrange
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);

        // Act
        CartaoDeCredito savedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Assert
        Optional<CartaoDeCredito> retrievedCartaoDeCredito = cartaoDeCreditoRepository.findByCiclistaId(1);
        assertTrue(retrievedCartaoDeCredito.isPresent());
        assertEquals(savedCartaoDeCredito.getId(), retrievedCartaoDeCredito.get().getId());
    }

    @Test
    void testFindByCiclistaId_Success() {
        // Arrange
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);
        cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Act
        Optional<CartaoDeCredito> result = cartaoDeCreditoRepository.findByCiclistaId(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getIdCiclista());
    }

    @Test
    void testFindByCiclistaId_NotFound() {
        // Act
        Optional<CartaoDeCredito> result = cartaoDeCreditoRepository.findByCiclistaId(1);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll() {
        // Arrange
        CartaoDeCredito cartao1 = new CartaoDeCredito();
        cartao1.setIdCiclista(1);

        CartaoDeCredito cartao2 = new CartaoDeCredito();
        cartao2.setIdCiclista(2);

        cartaoDeCreditoRepository.save(cartao1);
        cartaoDeCreditoRepository.save(cartao2);

        // Act
        List<CartaoDeCredito> result = cartaoDeCreditoRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteCartaoDeCredito() {
        // Arrange
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);
        CartaoDeCredito savedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Act
        cartaoDeCreditoRepository.delete(savedCartaoDeCredito);
        Optional<CartaoDeCredito> result = cartaoDeCreditoRepository.findByCiclistaId(1);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        // Arrange
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);

        // Act
        CartaoDeCredito savedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Assert
        assertNotNull(savedCartaoDeCredito);
        assertEquals(1, savedCartaoDeCredito.getIdCiclista());
    }
}
