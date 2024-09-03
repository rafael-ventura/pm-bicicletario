package com.example.bicicletario.Unitario.Services;

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
    void testGetFuncionario() {
        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setId(1);
        funcionarioMock.setNome("Funcionario");

        // Mockando a resposta do RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenReturn(new ResponseEntity<>(funcionarioMock, HttpStatus.OK));

        Funcionario funcionario = funcionarioService.get(1);

        assertNotNull(funcionario);
    }

    @Test
    void testIsFuncionarioValido() {
        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setNome("Funcionario");
        funcionarioMock.setCpf("12345678900");
        funcionarioMock.setId(1);

        // Mockando a resposta do RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenReturn(new ResponseEntity<>(funcionarioMock, HttpStatus.OK));

        boolean isValido = funcionarioService.isFuncionarioValido(1);

        assertFalse(isValido);
    }

    @Test
    void testGetFuncionario_NotFound() {
        // Mockando uma exceção 404 do RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Funcionario funcionario = funcionarioService.get(1);

        assertNotNull(funcionario);
    }
}

