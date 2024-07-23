package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.web.CiclistaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testCadastrarCiclista() {
        NovoCiclistaDTO novoCiclistaDTO = new NovoCiclistaDTO();
        NovoCiclistaRequestDTO request = new NovoCiclistaRequestDTO();
        request.setCiclista(novoCiclistaDTO);

        Ciclista ciclista = new Ciclista();
        when(ciclistaService.cadastrarCiclista(request)).thenReturn(ciclista);

        ResponseEntity<?> response = ciclistaController.cadastrarCiclista(request);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(ciclista, response.getBody());
    }

    @Test
    void testObterCiclista() {
        Ciclista ciclista = new Ciclista();
        when(ciclistaService.obterCiclista(1)).thenReturn(Optional.of(ciclista));

        ResponseEntity<Ciclista> response = ciclistaController.obterCiclista(1);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(ciclista, response.getBody());
    }

    @Test
    void testObterCiclista_NotFound() {
        when(ciclistaService.obterCiclista(1)).thenReturn(Optional.empty());

        ResponseEntity<Ciclista> response = ciclistaController.obterCiclista(1);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testAlterarCiclista() {
        NovoCiclistaDTO novoCiclistaDTO = new NovoCiclistaDTO();
        Ciclista ciclista = new Ciclista();
        when(ciclistaService.alterarCiclista(1, novoCiclistaDTO)).thenReturn(ciclista);

        ResponseEntity<Ciclista> response = ciclistaController.alterarCiclista(1, novoCiclistaDTO);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(ciclista, response.getBody());
    }

    @Test
    void testAtivarCiclista() {
        Ciclista ciclista = new Ciclista();
        when(ciclistaService.ativarCiclista(1)).thenReturn(ciclista);

        ResponseEntity<Ciclista> response = ciclistaController.ativarCiclista(1);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(ciclista, response.getBody());
    }

    @Test
    void testPermiteAluguel() {
        when(ciclistaService.permiteAluguel(1)).thenReturn(true);

        ResponseEntity<Boolean> response = ciclistaController.permiteAluguel(1);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody());
    }

    @Test
    void testObterBicicletaAlugada() {
        when(ciclistaService.obterBicicletaAlugada(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = ciclistaController.obterBicicletaAlugada(1);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(Optional.empty(), response.getBody());
    }

    @Test
    void testExisteEmail() {
        when(ciclistaService.existeEmail("test@example.com")).thenReturn(true);

        ResponseEntity<Boolean> response = ciclistaController.existeEmail("test@example.com");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody());
    }
}