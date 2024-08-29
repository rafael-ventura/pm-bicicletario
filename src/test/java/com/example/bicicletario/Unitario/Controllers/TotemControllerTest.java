package com.example.bicicletario.Unitario.Controllers;

import com.example.bicicletario.bicicletario.application.services.TotemService;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.web.TotemController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TotemControllerTest {

    @Mock
    private TotemService totemService;

    @InjectMocks
    private TotemController totemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTotens_Success() {
        // Arrange
        Totem totem = new Totem();
        totem.setId(1L);
        when(totemService.listarTodosTotens()).thenReturn(List.of(totem));

        // Act
        ResponseEntity<List<Totem>> response = totemController.listarTotens();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(totem.getId(), response.getBody().get(0).getId());
    }

    @Test
    void criarTotem_Success() {
        // Arrange
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        Totem totem = new Totem();
        totem.setId(1L);
        when(totemService.cadastrarNovoTotem(novoTotem)).thenReturn(totem);

        // Act
        ResponseEntity<Totem> response = totemController.criarTotem(novoTotem);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(totem.getId(), response.getBody().getId());
    }

    @Test
    void editarTotem_Success() {
        // Arrange
        Long totemId = 1L;
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        Totem totem = new Totem();
        totem.setId(totemId);
        when(totemService.atualizarTotem(totemId, novoTotem)).thenReturn(totem);

        // Act
        ResponseEntity<Totem> response = totemController.editarTotem(totemId, novoTotem);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(totemId, response.getBody().getId());
    }

    @Test
    void removerTotem_Success() {
        // Arrange
        Long totemId = 1L;

        // Act
        ResponseEntity<String> response = totemController.removerTotem(totemId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Totem removido", response.getBody());
    }

    @Test
    void listarTrancas_Success() {
        // Arrange
        Long totemId = 1L;
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        when(totemService.listarTrancasPorTotem(totemId)).thenReturn(List.of(tranca));

        // Act
        ResponseEntity<List<Tranca>> response = totemController.listarTrancas(totemId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(tranca.getId(), response.getBody().get(0).getId());
    }

    @Test
    void listarBicicletas_Success() {
        // Arrange
        Long totemId = 1L;
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        when(totemService.listarBicicletasPorTotem(totemId)).thenReturn(List.of(bicicleta));

        // Act
        ResponseEntity<List<Bicicleta>> response = totemController.listarBicicletas(totemId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(bicicleta.getId(), response.getBody().get(0).getId());
    }
}
