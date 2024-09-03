package com.example.bicicletario.Unitario.Repositories;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        totem.setId(null);

        Totem savedTotem = totemRepository.save(totem);

        assertNotNull(savedTotem.getId());
        Optional<Totem> retrievedTotem = totemRepository.findById(savedTotem.getId());
        assertTrue(retrievedTotem.isPresent());
        assertEquals(savedTotem.getId(), retrievedTotem.get().getId());
    }

    @Test
    void testDeleteById() {
        Totem totem = new Totem();
        totem.setId(null);

        Totem savedTotem = totemRepository.save(totem);
        totemRepository.deleteById(savedTotem.getId());

        Optional<Totem> retrievedTotem = totemRepository.findById(savedTotem.getId());
        assertFalse(retrievedTotem.isPresent());
    }

    @Test
    void testExistsById() {
        Totem totem = new Totem();
        totem.setId(null);

        Totem savedTotem = totemRepository.save(totem);

        boolean exists = totemRepository.existsById(savedTotem.getId());
        assertTrue(exists);
    }

    @Test
    void testExistsById_NotFound() {
        boolean exists = totemRepository.existsById(999);
        assertFalse(exists);
    }

    @Test
    void testFindAll() {
        Totem totem1 = new Totem();
        Totem totem2 = new Totem();

        totemRepository.save(totem1);
        totemRepository.save(totem2);

        List<Totem> totems = totemRepository.findAll();
        assertEquals(2, totems.size());
    }

    @Test
    void testFindAll_Empty() {
        List<Totem> totems = totemRepository.findAll();
        assertTrue(totems.isEmpty());
    }

    @Test
    void testSave_Update() {
        Totem totem = new Totem();
        totem.setId(null);
        totem.setDescricao("Descrição Original");

        Totem savedTotem = totemRepository.save(totem);

        // Atualizar a descrição do totem
        savedTotem.setDescricao("Descrição Atualizada");
        Totem updatedTotem = totemRepository.save(savedTotem);

        assertEquals("Descrição Atualizada", updatedTotem.getDescricao());

        Optional<Totem> retrievedTotem = totemRepository.findById(updatedTotem.getId());
        assertTrue(retrievedTotem.isPresent());
        assertEquals("Descrição Atualizada", retrievedTotem.get().getDescricao());
    }

    @Test
    void testDeleteById_NotFound() {
        // Tentar deletar um totem por um ID inexistente
        totemRepository.deleteById(999);

        // Garantir que nenhuma exceção foi lançada e o repositório ainda está consistente
        assertTrue(totemRepository.findAll().isEmpty());
    }
}
