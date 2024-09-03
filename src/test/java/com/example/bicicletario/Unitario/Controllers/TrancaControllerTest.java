package com.example.bicicletario.Unitario.Controllers;

import com.example.bicicletario.bicicletario.application.services.TrancaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
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

class TrancaControllerTest {

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
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dados cadastrados", response.getBody());
        verify(trancaService).incluirTrancaNaRede(dto);
    }

    @Test
    void retirarDaRede_Success() {
        // Arrange
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();

        // Act
        ResponseEntity<String> response = trancaController.retirarDaRede(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dados cadastrados", response.getBody());
        verify(trancaService).retirarTrancaDaRede(dto);
    }

    @Test
    void listarTrancas_Success() {
        // Arrange
        Tranca tranca = new Tranca();
        tranca.setId(1);
        when(trancaService.listarTodasTrancas()).thenReturn(List.of(tranca));

        // Act
        ResponseEntity<List<Tranca>> response = trancaController.listarTrancas();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(tranca.getId(), response.getBody().get(0).getId());
    }

    @Test
    void cadastrarTranca_Success() {
        // Arrange
        NovaTrancaDTO novaTrancaDTO = new NovaTrancaDTO();
        Tranca tranca = new Tranca();
        tranca.setId(1);
        when(trancaService.cadastrarNovaTranca(novaTrancaDTO)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.cadastrarTranca(novaTrancaDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(tranca.getId(), response.getBody().getId());
    }

    @Test
    void obterTranca_Success() {
        // Arrange
        Integer idTranca = 1;
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        when(trancaService.obterTrancaPorId(idTranca)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.obterTranca(idTranca);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void editarTranca_Success() {
        // Arrange
        Integer idTranca = 1;
        NovaTrancaDTO novaTrancaDTO = new NovaTrancaDTO();
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        when(trancaService.atualizarTranca(idTranca, novaTrancaDTO)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.editarTranca(idTranca, novaTrancaDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void removerTranca_Success() {
        // Arrange
        Integer idTranca = 1;

        // Act
        ResponseEntity<String> response = trancaController.removerTranca(idTranca);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Tranca removida", response.getBody());
        verify(trancaService).excluirTranca(idTranca);
    }

    @Test
    void obterBicicletaNaTranca_Success() {
        // Arrange
        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.OCUPADA);
        tranca.setLocalizacao("Localização 1");
        tranca.setModelo("Modelo 1");
        tranca.setNumero(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("Marca 1");
        bicicleta.setModelo("Modelo 1");
        tranca.setBicicleta(bicicleta);

        when(trancaService.obterBicicletaNaTranca(1)).thenReturn(tranca.getBicicleta());

        // Act
        ResponseEntity<Bicicleta> response = trancaController.obterBicicletaNaTranca(1);

        // Assert

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void trancarTranca_Success() {
        // Arrange
        Integer idTranca = 1;
        Integer bicicletaId = 1;
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaService.trancarTranca(idTranca, bicicletaId)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.trancarTranca(idTranca, bicicletaId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void destrancarTranca_Success() {
        // Arrange
        Integer idTranca = 1;
        Integer bicicletaId = 1;
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaService.destrancarTranca(idTranca, bicicletaId)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.destrancarTranca(idTranca, bicicletaId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }

    @Test
    void alterarStatusTranca_Success() {
        // Arrange
        Integer idTranca = 1;
        String acao = "trancar";
        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaService.alterarStatusTranca(idTranca, acao)).thenReturn(tranca);

        // Act
        ResponseEntity<Tranca> response = trancaController.alterarStatusTranca(idTranca, acao);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idTranca, response.getBody().getId());
    }
}

