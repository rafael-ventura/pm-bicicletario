package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioServiceTest {

    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        funcionarioService = new FuncionarioService();
    }

    @Test
    void testGetFuncionario() {
        Funcionario funcionario = funcionarioService.get(1L);
        assertNotNull(funcionario);
        assertEquals(1L, funcionario.getId());
        assertEquals("Funcionario", funcionario.getNome());
    }

    @Test
    void testIsFuncionarioValido() {
        boolean isValido = funcionarioService.isFuncionarioValido(1L);
        assertFalse(isValido);
    }
}
