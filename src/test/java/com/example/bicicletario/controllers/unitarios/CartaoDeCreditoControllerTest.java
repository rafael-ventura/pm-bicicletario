package com.example.bicicletario.controllers.unitarios;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.web.CartaoDeCreditoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartaoDeCreditoControllerTest {

    @Mock
    private CartaoDeCreditoService cartaoDeCreditoService;

    @InjectMocks
    private CartaoDeCreditoController cartaoDeCreditoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obterCartaoDeCredito_Success() {
        // Arrange
        int idCiclista = 1;
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(idCiclista);
        when(cartaoDeCreditoService.obterCartaoDeCredito(idCiclista)).thenReturn(cartaoDeCredito);

        // Act
        ResponseEntity<CartaoDeCredito> response = cartaoDeCreditoController.obterCartaoDeCredito(idCiclista);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(idCiclista, response.getBody().getId());
    }

    @Test
    void alterarCartaoDeCredito_Success() {
        // Arrange
        int idCiclista = 1;
        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        doNothing().when(cartaoDeCreditoService).alterarCartaoDeCredito(idCiclista, novoCartaoDeCreditoDTO);

        // Act
        ResponseEntity<NovoCartaoDeCreditoDTO> response = cartaoDeCreditoController.alterarCartaoDeCredito(idCiclista, novoCartaoDeCreditoDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(cartaoDeCreditoService).alterarCartaoDeCredito(idCiclista, novoCartaoDeCreditoDTO);
    }
}
