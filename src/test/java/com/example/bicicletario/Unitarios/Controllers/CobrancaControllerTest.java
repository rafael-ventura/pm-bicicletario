//package com.example.bicicletario.Unitarios.Controllers;
//
//import com.example.bicicletario.bicicletario.application.CobrancaService;
//import com.example.bicicletario.bicicletario.domain.Cobranca;
//import com.example.bicicletario.bicicletario.domain.Erro;
//import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
//import com.example.bicicletario.bicicletario.web.CobrancaController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//class CobrancaControllerTest {
//
//    @Mock
//    private CobrancaService cobrancaService;
//
//    @InjectMocks
//    private CobrancaController cobrancaController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void realizarCobranca_Success() {
//        // Arrange
//        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();
//        Cobranca cobranca = new Cobranca();
//        cobranca.setId(1);
//
//        when(cobrancaService.realizarCobranca(novaCobranca)).thenReturn(cobranca);
//
//        // Act
//        ResponseEntity<Object> response = cobrancaController.realizarCobranca(novaCobranca);
//
//        // Assert
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(cobranca.getId(), ((Cobranca) response.getBody()).getId());
//    }
//
//    @Test
//    void realizarCobranca_InvalidData() {
//        // Arrange
//        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();
//
//        when(cobrancaService.realizarCobranca(novaCobranca)).thenThrow(new RuntimeException("Dados Inválidos"));
//
//        // Act
//        ResponseEntity<Object> response = cobrancaController.realizarCobranca(novaCobranca);
//
//        // Assert
//        assertEquals(422, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("422", ((Erro) response.getBody()).getCodigo());
//        assertEquals("Dados Inválidos", ((Erro) response.getBody()).getMensagem());
//    }
//
//    @Test
//    void obterCobranca_Success() {
//        // Arrange
//        int idCobranca = 1;
//        Cobranca cobranca = new Cobranca();
//        cobranca.setId(idCobranca);
//
//        when(cobrancaService.obterCobrancaPorId(idCobranca)).thenReturn(cobranca);
//
//        // Act
//        ResponseEntity<Object> response = cobrancaController.obterCobranca(idCobranca);
//
//        // Assert
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(idCobranca, ((Cobranca) response.getBody()).getId());
//    }
//
//    @Test
//    void obterCobranca_NotFound() {
//        // Arrange
//        int idCobranca = 1;
//
//        when(cobrancaService.obterCobrancaPorId(idCobranca)).thenReturn(null);
//
//        // Act
//        ResponseEntity<Object> response = cobrancaController.obterCobranca(idCobranca);
//
//        // Assert
//        assertEquals(404, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("404", ((Erro) response.getBody()).getCodigo());
//        assertEquals("Cobrança não encontrada", ((Erro) response.getBody()).getMensagem());
//    }
//}
