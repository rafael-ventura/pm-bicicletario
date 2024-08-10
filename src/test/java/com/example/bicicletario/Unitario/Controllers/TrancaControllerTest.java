package com.example.bicicletario.Unitario.Controllers;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.web.TrancaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrancaControllerTest {

    @Mock
    private TrancaService trancaService;

    @InjectMocks
    private TrancaController trancaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void integrarNaRede_Success() {
        // Arrange
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();

        // Act
        ResponseEntity<String> response = trancaController.integrarNaRede(dto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(trancaService).integrarNaRede(dto);
    }

    @Test
    void retirarDaRede_Success() {
        // Arrange
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();

        // Act
        ResponseEntity<String> response = trancaController.retirarDaRede(dto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(trancaService).retirarDaRede(dto);
    }

    @Test
    void listarTrancas_Success() {
        // Arrange
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        when(trancaService.listarTrancas()).thenReturn(List.of(tranca));

        // Act
        ResponseEntity<List<Tranca>> response = trancaController.listarTrancas();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(tranca.getId(), response.getBody().get(0).getId());
    }

    @Test
    void cadastrarTranca_Success() {
        // Arrange
        NovaTrancaDTO novaTrancaDTO = new NovaTrancaDTO();
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        when(trancaService.cadastrarTranca(novaTrancaDTO)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.cadastrarTranca(novaTrancaDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(tranca.getId(), response.getBody().getId());
    }

    @Test
    void obterTranca_Success() {
        // Arrange
        Long idTranca = 1L;
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.obterTranca(idTranca);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void editarTranca_Success() {
        // Arrange
        Long idTranca = 1L;
        NovaTrancaDTO novaTrancaDTO = new NovaTrancaDTO();
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        when(trancaService.editarTranca(idTranca, novaTrancaDTO)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.editarTranca(idTranca, novaTrancaDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void removerTranca_Success() {
        // Arrange
        Long idTranca = 1L;

        // Act
        ResponseEntity<String> response = trancaController.removerTranca(idTranca);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tranca removida com sucesso", response.getBody());
        verify(trancaService).removerTranca(idTranca);
    }

    @Test
    void obterBicicletaNaTranca_Success() {
        // Arrange
        Long idTranca = 1L;
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        when(trancaService.obterBicicletaNaTranca(idTranca)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.obterBicicletaNaTranca(idTranca);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void trancarTranca_Success() {
        // Arrange
        Long idTranca = 1L;
        Long bicicletaId = 1L;

        // Act
        ResponseEntity<String> response = trancaController.trancarTranca(idTranca, bicicletaId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(trancaService).trancarTranca(idTranca, bicicletaId);
    }

    @Test
    void destrancarTranca_Success() {
        // Arrange
        Long idTranca = 1L;
        Long bicicletaId = 1L;

        // Act
        ResponseEntity<String> response = trancaController.destrancarTranca(idTranca, bicicletaId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(trancaService).destrancarTranca(idTranca, bicicletaId);
    }

    @Test
    void alterarStatusTranca_Success() {
        // Arrange
        Long idTranca = 1L;
        String acao = "ativar";

        // Act
        ResponseEntity<String> response = trancaController.alterarStatusTranca(idTranca, acao);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(trancaService).alterarStatusTranca(idTranca, acao);
    }
}
