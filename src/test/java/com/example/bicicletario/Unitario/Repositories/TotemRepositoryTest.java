package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TotemRepositoryTest {

    @Mock
    private TotemRepository totemRepository;

    @BeforeEach
    void setUp() {
        totemRepository = new TotemRepository();
    }

    @Test
    void testSaveAndFindById() {
        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemRepository.findById(1)).thenReturn(Optional.of(totem));

        totemRepository.save(totem);

        Optional<Totem> retrievedTotem = totemRepository.findById(1);
        assertTrue(retrievedTotem.isPresent());
        assertEquals(1, retrievedTotem.get().getId());
        assertEquals("Localizacao", retrievedTotem.get().getLocalizacao());
    }

    @Test
    void testDeleteById() {
        Totem totem = new Totem();
        totem.setId(1);

        totemRepository.save(totem);
        totemRepository.deleteById(1);

        Optional<Totem> retrievedTotem = totemRepository.findById(1);
        assertFalse(retrievedTotem.isPresent());
    }

    @Test
    void testFindAll() {
        Totem totem1 = new Totem();
        totem1.setLocalizacao("Localizacao 1");
        totem1.setDescricao("Descricao 1");

        Totem totem2 = new Totem();
        totem2.setLocalizacao("Localizacao 2");
        totem2.setDescricao("Descricao 2");

        totemRepository.save(totem1);
        totemRepository.save(totem2);

        List<Totem> result = totemRepository.findAll();
        assertEquals(2, result.size());
    }

}
