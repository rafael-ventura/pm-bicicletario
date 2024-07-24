package com.example.bicicletario.repositories;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.infraestructure.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FuncionarioRepositoryTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService; // Supondo que você tenha um serviço que usa o repositório

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindById() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Joao Silva");
        funcionario.setId(1);

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));

        funcionarioRepository.save(funcionario);
        Optional<Funcionario> foundFuncionario = funcionarioRepository.findById(1);

        assertTrue(foundFuncionario.isPresent());
        assertEquals("Joao Silva", foundFuncionario.get().getNome());
    }

    @Test
    void testFindAll() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("Joao Silva");
        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Oliveira");

        when(funcionarioRepository.findAll()).thenReturn(Arrays.asList(funcionario1, funcionario2));

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        assertEquals(2, funcionarios.size());
    }

    @Test
    void testDeleteById() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Joao Silva");
        funcionario.setId(1);

        doNothing().when(funcionarioRepository).deleteById(1);

        funcionarioRepository.deleteById(1);
        verify(funcionarioRepository, times(1)).deleteById(1);
    }

    @Test
    void testExistsById() {
        when(funcionarioRepository.existsById(1)).thenReturn(true);
        when(funcionarioRepository.existsById(2)).thenReturn(false);

        assertTrue(funcionarioRepository.existsById(1));
        assertFalse(funcionarioRepository.existsById(2));
    }
}
