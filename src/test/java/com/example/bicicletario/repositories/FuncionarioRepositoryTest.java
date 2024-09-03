package com.example.bicicletario.repositories;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.infraestructure.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioRepositoryTest {

    private FuncionarioRepository funcionarioRepository;

    @BeforeEach
    void setUp() {
        funcionarioRepository = new FuncionarioRepository();
    }

    @Test
    void testSaveAndFindById() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Joao Silva");

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        assertNotNull(savedFuncionario.getId());
        assertEquals("Joao Silva", savedFuncionario.getNome());

        Optional<Funcionario> foundFuncionario = funcionarioRepository.findById(savedFuncionario.getId());

        assertTrue(foundFuncionario.isPresent());
        assertEquals("Joao Silva", foundFuncionario.get().getNome());
    }

    @Test
    void testFindAll() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("Joao Silva");
        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Oliveira");

        funcionarioRepository.save(funcionario1);
        funcionarioRepository.save(funcionario2);

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        assertEquals(2, funcionarios.size());
        assertTrue(funcionarios.stream().anyMatch(f -> "Joao Silva".equals(f.getNome())));
        assertTrue(funcionarios.stream().anyMatch(f -> "Maria Oliveira".equals(f.getNome())));
    }

    @Test
    void testDeleteById() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Joao Silva");

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        funcionarioRepository.deleteById(savedFuncionario.getId());
        Optional<Funcionario> foundFuncionario = funcionarioRepository.findById(savedFuncionario.getId());

        assertFalse(foundFuncionario.isPresent());
    }

    @Test
    void testExistsById() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Joao Silva");

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        assertTrue(funcionarioRepository.existsById(savedFuncionario.getId()));
        assertFalse(funcionarioRepository.existsById(999)); // ID not present

    }

    @Test
    void testDeleteFuncionario() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João");
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        // Act
        funcionarioRepository.delete(savedFuncionario);
        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(savedFuncionario.getId());

        // Assert
        assertFalse(retrievedFuncionario.isPresent());
    }

    @Test
    void testExistsById_Success() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João");
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        // Act
        boolean exists = funcionarioRepository.existsById(savedFuncionario.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsById_NotFound() {
        // Act
        boolean exists = funcionarioRepository.existsById(999);

        // Assert
        assertFalse(exists);
    }

}
