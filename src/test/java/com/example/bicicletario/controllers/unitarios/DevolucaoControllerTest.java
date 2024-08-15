package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.DevolucaoService;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class DevolucaoControllerTest {

    @Mock
    private DevolucaoService devolucaoService;

    @InjectMocks
    private DevolucaoController devolucaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void realizarDevolucao_Success() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);

        when(devolucaoService.realizarDevolucao(any(NovoDevolucaoDTO.class))).thenReturn(devolucao);

        // Act
        ResponseEntity<Devolucao> response = devolucaoController.realizarDevolucao(devolucaoDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getIdBicicleta());
        assertEquals(2, response.getBody().getIdTranca());
    }

    @Test
    void realizarDevolucao_InvalidDataException() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        when(devolucaoService.realizarDevolucao(any(NovoDevolucaoDTO.class)))
                .thenThrow(new InvalidDataException("Dados inválidos"));

        // Act & Assert
        InvalidDataException thrown = assertThrows(InvalidDataException.class, () ->
                devolucaoController.realizarDevolucao(devolucaoDTO));

        assertEquals("Dados inválidos", thrown.getMessage());
        verify(devolucaoService).realizarDevolucao(devolucaoDTO);
    }

    @Test
    void realizarDevolucao_ResourceNotFoundException() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        when(devolucaoService.realizarDevolucao(any(NovoDevolucaoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->
                devolucaoController.realizarDevolucao(devolucaoDTO));

        assertEquals("Recurso não encontrado", thrown.getMessage());
        verify(devolucaoService).realizarDevolucao(devolucaoDTO);
    }

    @Test
    void realizarDevolucao_EmptyRequest() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();  // Empty request

        // Simula o lançamento da InvalidDataException no serviço
        when(devolucaoService.realizarDevolucao(devolucaoDTO)).thenThrow(new InvalidDataException("Dados inválidos"));

        // Act & Assert
        InvalidDataException thrown = assertThrows(InvalidDataException.class, () ->
                devolucaoController.realizarDevolucao(devolucaoDTO));

        assertEquals("Dados inválidos", thrown.getMessage());
        verify(devolucaoService).realizarDevolucao(devolucaoDTO);
    }
}
