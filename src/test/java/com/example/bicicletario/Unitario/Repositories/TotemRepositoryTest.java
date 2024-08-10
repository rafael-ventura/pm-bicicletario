package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TotemRepositoryTest {

    private TotemRepository totemRepository;

    @BeforeEach
    void setUp() {
        totemRepository = new TotemRepository();
    }

    @Test
    void testSaveAndFindById() {
        Totem totem = new Totem();
        totem.setId(1L);

        Totem savedTotem = totemRepository.save(totem);

        assertEquals(1L, savedTotem.getId());
        Optional<Totem> retrievedTotem = totemRepository.findById(1L);
        assertTrue(retrievedTotem.isPresent());
        assertEquals(1L, retrievedTotem.get().getId());
    }

    @Test
    void testDeleteById() {
        Totem totem = new Totem();
        totem.setId(1L);

        totemRepository.save(totem);
        totemRepository.deleteById(1L);

        Optional<Totem> retrievedTotem = totemRepository.findById(1L);
        assertFalse(retrievedTotem.isPresent());
    }

    @Test
    void testFindAll() {
        Totem totem1 = new Totem();
        totem1.setId(1L);
        totemRepository.save(totem1);

        Totem totem2 = new Totem();
        totem2.setId(2L);
        totemRepository.save(totem2);

        assertEquals(2, totemRepository.findAll().size());
    }
}
