package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class FuncionarioServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFuncionarioComSucesso() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);

        ResponseEntity<Funcionario> responseEntity = new ResponseEntity<>(funcionario, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class))).thenReturn(responseEntity);

        // Act
        Funcionario resultado = funcionarioService.get(1);

        // Assert
        assertEquals(1, resultado.getId());
    }

    @Test
    void getFuncionarioNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.get(1);
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }

    @Test
    void getFuncionarioErroGenerico_DeveLancarExcecao() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.get(1);
        });

        assertEquals("Erro ao buscar funcionário: 500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    @Test
    void isFuncionarioValido_DeveRetornarTrueSeFuncionarioValido() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);

        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenReturn(new ResponseEntity<>(funcionario, HttpStatus.OK));

        // Act
        boolean resultado = funcionarioService.isFuncionarioValido(1);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void isFuncionarioValido_DeveRetornarFalseSeFuncionarioNaoValido() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(2); // id diferente do que foi passado no método

        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenReturn(new ResponseEntity<>(funcionario, HttpStatus.OK));

        // Act
        boolean resultado = funcionarioService.isFuncionarioValido(1);

        // Assert
        assertFalse(resultado); // Espera-se que retorne false porque o ID é diferente
    }

}
