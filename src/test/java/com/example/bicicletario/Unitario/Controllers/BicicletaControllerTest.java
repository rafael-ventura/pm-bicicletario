package com.example.bicicletario.Unitario.Controllers;

import com.example.bicicletario.bicicletario.application.services.BicicletaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.web.BicicletaController;
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

class BicicletaControllerTest {

    @Mock
    private BicicletaService bicicletaService;

    @InjectMocks
    private BicicletaController bicicletaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarBicicletas_Success() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        when(bicicletaService.listarBicicletas()).thenReturn(List.of(bicicleta));

        // Act
        ResponseEntity<List<Bicicleta>> response = bicicletaController.listarBicicletas();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(bicicleta.getId(), response.getBody().get(0).getId());
    }

    @Test
    void cadastrarBicicleta_Success() {
        // Arrange
        NovaBicicletaDTO novaBicicletaDTO = new NovaBicicletaDTO();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        when(bicicletaService.cadastrarBicicleta(novaBicicletaDTO)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.cadastrarBicicleta(novaBicicletaDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(bicicleta.getId(), response.getBody().getId());
    }

    @Test
    void integrarNaRede_Success() {
        // Arrange
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();

        // Act
        ResponseEntity<String> response = bicicletaController.integrarNaRede(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dados cadastrados", response.getBody());
        verify(bicicletaService).integrarBicicletaNaRede(dto);
    }

    @Test
    void retirarBicicletaDaRede_Success() {
        // Arrange
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();

        // Act
        ResponseEntity<String> response = bicicletaController.retirarDaRede(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dados cadastrados", response.getBody());
        verify(bicicletaService).retirarBicicletaDaRede(dto);
    }

    @Test
    void obterBicicleta_Success() {
        // Arrange
        Integer idBicicleta = 1;
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        when(bicicletaService.obterBicicletaPorId(idBicicleta)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.obterBicicleta(idBicicleta);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idBicicleta, response.getBody().getId());
    }

    @Test
    void atualizarBicicleta_Success() {
        // Arrange
        Integer idBicicleta = 1;
        NovaBicicletaDTO novaBicicletaDTO = new NovaBicicletaDTO();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        when(bicicletaService.atualizarBicicleta(idBicicleta, novaBicicletaDTO)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.atualizarBicicleta(idBicicleta, novaBicicletaDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idBicicleta, response.getBody().getId());
    }

    @Test
    void removerBicicleta_Success() {
        // Arrange
        Integer idBicicleta = 1;

        // Act
        ResponseEntity<String> response = bicicletaController.removerBicicleta(idBicicleta);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Bicicleta removida", response.getBody());
        verify(bicicletaService).excluirBicicleta(idBicicleta);
    }

    @Test
    void alterarStatusBicicleta_Success() {
        // Arrange
        Integer idBicicleta = 1;
        String acao = "ativar";
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        when(bicicletaService.alterarStatusBicicleta(idBicicleta, acao)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.alterarStatusBicicleta(idBicicleta, acao);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idBicicleta, response.getBody().getId());
        verify(bicicletaService).alterarStatusBicicleta(idBicicleta, acao);
    }
}
