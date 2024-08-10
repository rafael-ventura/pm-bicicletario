package com.example.bicicletario.repositories.unitarios;

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
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João");

        // Act
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        // Assert
        assertNotNull(savedFuncionario.getId());
        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(savedFuncionario.getId());
        assertTrue(retrievedFuncionario.isPresent());
        assertEquals("João", retrievedFuncionario.get().getNome());
    }

    @Test
    void testFindAll() {
        // Arrange
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("João");

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria");

        funcionarioRepository.save(funcionario1);
        funcionarioRepository.save(funcionario2);

        // Act
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        // Assert
        assertNotNull(funcionarios);
        assertEquals(2, funcionarios.size());
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

    @Test
    void testDeleteById() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João");
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        // Act
        funcionarioRepository.deleteById(savedFuncionario.getId());
        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(savedFuncionario.getId());

        // Assert
        assertFalse(retrievedFuncionario.isPresent());
    }
}
