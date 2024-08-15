package com.example.bicicletario.Unitarios.Controllers;

import com.example.bicicletario.bicicletario.application.FilaCobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.web.FilaCobrancaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FilaCobrancaControllerTest {

    @Mock
    private FilaCobrancaService filaCobrancaService;

    @InjectMocks
    private FilaCobrancaController filaCobrancaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adicionarNaFila_Success() {
        // Arrange
        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1);

        when(filaCobrancaService.adicionarNaFila(novaCobranca)).thenReturn(cobranca);

        // Act
        ResponseEntity<Object> response = filaCobrancaController.adicionarNaFila(novaCobranca);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(cobranca.getId(), ((Cobranca) response.getBody()).getId());
    }

    @Test
    void adicionarNaFila_InvalidArgument() {
        // Arrange
        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();

        when(filaCobrancaService.adicionarNaFila(novaCobranca)).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act
        ResponseEntity<Object> response = filaCobrancaController.adicionarNaFila(novaCobranca);

        // Assert
        assertEquals(422, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("422", ((Erro) response.getBody()).getCodigo());
        assertEquals("Dados inválidos", ((Erro) response.getBody()).getMensagem());
    }

    @Test
    void adicionarNaFila_ServerError() {
        // Arrange
        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();

        when(filaCobrancaService.adicionarNaFila(novaCobranca)).thenThrow(new RuntimeException("Erro interno no servidor"));

        // Act
        ResponseEntity<Object> response = filaCobrancaController.adicionarNaFila(novaCobranca);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("500", ((Erro) response.getBody()).getCodigo());
        assertEquals("Erro interno no servidor", ((Erro) response.getBody()).getMensagem());
    }

    @Test
    void processarFila_Success() {
        // Arrange
        List<Cobranca> cobrancasProcessadas = List.of(new Cobranca());

        when(filaCobrancaService.processarFila()).thenReturn(cobrancasProcessadas);

        // Act
        ResponseEntity<Object> response = filaCobrancaController.processarFila();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    void processarFila_ServerError() {
        // Arrange
        when(filaCobrancaService.processarFila()).thenThrow(new RuntimeException("Erro ao processar fila"));

        // Act
        ResponseEntity<Object> response = filaCobrancaController.processarFila();

        // Assert
        assertEquals(422, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("422", ((Erro) response.getBody()).getCodigo());
        assertEquals("Erro ao processar fila", ((Erro) response.getBody()).getMensagem());
    }
}
