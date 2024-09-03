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
    void testFindByCiclistaId() {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);
        cartaoDeCreditoRepository.save(cartaoDeCredito);

        Optional<CartaoDeCredito> result = cartaoDeCreditoRepository.findByCiclistaId(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getIdCiclista());
    }

    @Test
    void testFindAll() {
        CartaoDeCredito cartaoDeCredito1 = new CartaoDeCredito();
        cartaoDeCredito1.setIdCiclista(1);
        CartaoDeCredito cartaoDeCredito2 = new CartaoDeCredito();
        cartaoDeCredito2.setIdCiclista(2);

        cartaoDeCreditoRepository.save(cartaoDeCredito1);
        cartaoDeCreditoRepository.save(cartaoDeCredito2);

        List<CartaoDeCredito> result = cartaoDeCreditoRepository.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testSave() {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);

        CartaoDeCredito savedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);
        assertEquals(1, savedCartaoDeCredito.getIdCiclista());
    }

    @Test
    void testDelete() {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setIdCiclista(1);
        CartaoDeCredito savedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        cartaoDeCreditoRepository.delete(savedCartaoDeCredito);
        Optional<CartaoDeCredito> result = cartaoDeCreditoRepository.findByCiclistaId(1);
        assertFalse(result.isPresent());
    }
}