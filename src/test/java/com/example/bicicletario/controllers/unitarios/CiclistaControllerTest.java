package com.example.bicicletario.controllers.unitarios;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.web.CiclistaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static com.example.bicicletario.bicicletario.application.Constants.CICLISTA_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CiclistaControllerTest {

    @Mock
    private CiclistaService ciclistaService;

    @InjectMocks
    private CiclistaController ciclistaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarCiclista_Success() {
        // Arrange
        NovoCiclistaRequestDTO request = new NovoCiclistaRequestDTO();
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        when(ciclistaService.cadastrarCiclista(request)).thenReturn(ciclista);

        // Act
        ResponseEntity<Ciclista> response = ciclistaController.cadastrarCiclista(request);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(ciclista.getId(), response.getBody().getId());
    }

    @Test
    void obterCiclista_Success() {
        // Arrange
        int idCiclista = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        when(ciclistaService.obterCiclista(idCiclista)).thenReturn(Optional.of(ciclista));

        // Act
        ResponseEntity<Ciclista> response = ciclistaController.obterCiclista(idCiclista);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idCiclista, response.getBody().getId());
    }

    @Test
    void obterCiclista_NotFound() {
        // Arrange
        int idCiclista = 1;
        when(ciclistaService.obterCiclista(idCiclista)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            ciclistaController.obterCiclista(idCiclista);
        } catch (ResourceNotFoundException e) {
            assertEquals(CICLISTA_NAO_ENCONTRADO, e.getMessage());
        }
    }

    @Test
    void alterarCiclista_Success() {
        // Arrange
        int idCiclista = 1;
        NovoCiclistaRequestDTO novoCiclistaDTO = new NovoCiclistaRequestDTO();
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        when(ciclistaService.alterarCiclista(idCiclista, novoCiclistaDTO)).thenReturn(ciclista);

        // Act
        ResponseEntity<Ciclista> response = ciclistaController.alterarCiclista(idCiclista, novoCiclistaDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idCiclista, response.getBody().getId());
    }

    @Test
    void ativarCiclista_Success() {
        // Arrange
        int idCiclista = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        when(ciclistaService.ativarCiclista(idCiclista)).thenReturn(ciclista);

        // Act
        ResponseEntity<Ciclista> response = ciclistaController.ativarCiclista(idCiclista);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idCiclista, response.getBody().getId());
    }

    @Test
    void permiteAluguel_Success() {
        // Arrange
        int idCiclista = 1;
        when(ciclistaService.permiteAluguel(idCiclista)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = ciclistaController.permiteAluguel(idCiclista);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody());
    }

    @Test
    void obterBicicletaAlugada_Success() {
        // Arrange
        int idCiclista = 1;
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        when(ciclistaService.obterBicicletaAlugada(idCiclista)).thenReturn(Optional.of(bicicleta));

        // Act
        ResponseEntity<Bicicleta> response = ciclistaController.obterBicicletaAlugada(idCiclista);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(bicicleta.getId(), response.getBody().getId());
    }

    @Test
    void obterBicicletaAlugada_NoContent() {
        // Arrange
        int idCiclista = 1;
        when(ciclistaService.obterBicicletaAlugada(idCiclista)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Bicicleta> response = ciclistaController.obterBicicletaAlugada(idCiclista);

        // Assert
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void existeEmail_Success() {
        // Arrange
        String email = "email@example.com";
        when(ciclistaService.existeEmail(email)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = ciclistaController.existeEmail(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody());
    }
}