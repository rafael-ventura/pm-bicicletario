package com.example.bicicletario.Unitarios.Controllers;

import com.example.bicicletario.bicicletario.application.ValidaCartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.web.ValidaCartaoDeCreditoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ValidaCartaoDeCreditoControllerTest {

    @Mock
    private ValidaCartaoDeCreditoService validaCartaoDeCreditoService;

    @InjectMocks
    private ValidaCartaoDeCreditoController validaCartaoDeCreditoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validarCartaoDeCredito_Success() {
        // Arrange
        NovoCartaoDeCreditoDTO cartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        cartaoDeCreditoDTO.setNumero("1234567812345678");
        cartaoDeCreditoDTO.setNomeTitular("João da Silva");
        cartaoDeCreditoDTO.setValidade("12/24");
        cartaoDeCreditoDTO.setCvv("123");

        when(validaCartaoDeCreditoService.validarCartao(cartaoDeCreditoDTO)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = validaCartaoDeCreditoController.validarCartaoDeCredito(cartaoDeCreditoDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dados atualizados", response.getBody());
    }

    @Test
    void validarCartaoDeCredito_DadosInvalidos() {
        // Arrange
        NovoCartaoDeCreditoDTO cartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        cartaoDeCreditoDTO.setNumero("1234567812345678");
        cartaoDeCreditoDTO.setNomeTitular("João da Silva");
        cartaoDeCreditoDTO.setValidade("12/24");
        cartaoDeCreditoDTO.setCvv("123");

        when(validaCartaoDeCreditoService.validarCartao(cartaoDeCreditoDTO)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = validaCartaoDeCreditoController.validarCartaoDeCredito(cartaoDeCreditoDTO);

        // Assert
        assertEquals(422, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("422", ((Erro) response.getBody()).getCodigo());
        assertEquals("Dados Inválidos", ((Erro) response.getBody()).getMensagem());
    }
}