package com.example.bicicletario.Unitario.Controllers;

import com.example.bicicletario.bicicletario.application.BicicletaService;
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

public class BicicletaControllerTest {

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
        bicicleta.setId(1L);
        when(bicicletaService.listarBicicletas()).thenReturn(List.of(bicicleta));

        // Act
        ResponseEntity<List<Bicicleta>> response = bicicletaController.listarBicicletas();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(bicicleta.getId(), response.getBody().get(0).getId());
    }

    @Test
    void criarBicicleta_Success() {
        // Arrange
        NovaBicicletaDTO novaBicicletaDTO = new NovaBicicletaDTO();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        when(bicicletaService.criarBicicleta(novaBicicletaDTO)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.criarBicicleta(novaBicicletaDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
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
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(bicicletaService).integrarNaRede(dto);
    }

    @Test
    void retirarDaRede_Success() {
        // Arrange
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();

        // Act
        ResponseEntity<String> response = bicicletaController.retirarDaRede(dto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dados cadastrados com sucesso", response.getBody());
        verify(bicicletaService).retirarDaRede(dto);
    }

    @Test
    void obterBicicleta_Success() {
        // Arrange
        Long idBicicleta = 1L;
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        when(bicicletaService.obterBicicleta(idBicicleta)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.obterBicicleta(idBicicleta);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(idBicicleta, response.getBody().getId());
    }

    @Test
    void editarBicicleta_Success() {
        // Arrange
        Long idBicicleta = 1L;
        NovaBicicletaDTO novaBicicletaDTO = new NovaBicicletaDTO();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        when(bicicletaService.editarBicicleta(idBicicleta, novaBicicletaDTO)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.editarBicicleta(idBicicleta, novaBicicletaDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(idBicicleta, response.getBody().getId());
    }

    @Test
    void removerBicicleta_Success() {
        // Arrange
        Long idBicicleta = 1L;

        // Act
        ResponseEntity<String> response = bicicletaController.removerBicicleta(idBicicleta);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bicicleta removida com sucesso", response.getBody());
        verify(bicicletaService).removerBicicleta(idBicicleta);
    }

    @Test
    void alterarStatusBicicleta_Success() {
        // Arrange
        Long idBicicleta = 1L;
        String acao = "ativar";
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        when(bicicletaService.alterarStatusBicicleta(idBicicleta, acao)).thenReturn(bicicleta);

        // Act
        ResponseEntity<Bicicleta> response = bicicletaController.alterarStatusBicicleta(idBicicleta, acao);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(idBicicleta, response.getBody().getId());
        verify(bicicletaService).alterarStatusBicicleta(idBicicleta, acao);
    }
}
