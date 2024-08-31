package com.example.bicicletario.controllers.unitarios;

import com.example.bicicletario.bicicletario.application.AluguelService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.dto.NovoAluguelDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.web.AluguelController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AluguelControllerTest {

    @Mock
    private AluguelService aluguelService;

    @InjectMocks
    private AluguelController aluguelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void alugarBicicleta_Success() {
        // Arrange
        NovoAluguelDTO novoAluguelDTO = new NovoAluguelDTO();
        novoAluguelDTO.setCiclista(1);
        novoAluguelDTO.setTrancaInicio(1);

        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);
        aluguel.setTrancaInicio(1);

        when(aluguelService.aluguel(1, 1)).thenReturn(aluguel);

        // Act
        ResponseEntity<Aluguel> response = aluguelController.alugarBicicleta(novoAluguelDTO);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCiclista());
        verify(aluguelService).aluguel(1, 1);
    }

    @Test
    void alugarBicicleta_InvalidRequest() {
        // Arrange
        NovoAluguelDTO novoAluguelDTO = new NovoAluguelDTO();
        novoAluguelDTO.setCiclista(1);
        novoAluguelDTO.setTrancaInicio(1);

        when(aluguelService.aluguel(1, 1)).thenThrow(new InvalidDataException("Ciclista já possui um aluguel ativo."));

        // Act & Assert
        InvalidDataException thrown = assertThrows(InvalidDataException.class, () ->
                aluguelController.alugarBicicleta(novoAluguelDTO));

        assertEquals("Ciclista já possui um aluguel ativo.", thrown.getMessage());
        verify(aluguelService).aluguel(1, 1);
    }
}
