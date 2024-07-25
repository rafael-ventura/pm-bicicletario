package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.utils.ErroUtil;
import com.example.bicicletario.bicicletario.domain.models.Erro;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ErroUtilTest {

    @Test
    void testCriarErro() {
        ResponseEntity<Erro> response = ErroUtil.criarErro(HttpStatus.BAD_REQUEST, "400", "Mensagem de erro");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().getCodigo());
        assertEquals("Mensagem de erro", response.getBody().getMensagem());
    }

    @Test
    void testErroInterno() {
        ResponseEntity<Erro> response = ErroUtil.erroInterno("Erro interno");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("500", response.getBody().getCodigo());
        assertEquals("Erro interno", response.getBody().getMensagem());
    }

    @Test
    void testErroNaoEncontrado() {
        ResponseEntity<Erro> response = ErroUtil.erroNaoEncontrado("Recurso não encontrado");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("404", response.getBody().getCodigo());
        assertEquals("Recurso não encontrado", response.getBody().getMensagem());
    }

    @Test
    void testErroInvalido() {
        ResponseEntity<Erro> response = ErroUtil.erroInvalido("Dados inválidos");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("422", response.getBody().getCodigo());
        assertEquals("Dados inválidos", response.getBody().getMensagem());
    }

    @Test
    void testPrivateConstructor() {
        assertThrows(IllegalStateException.class, ErroUtil::new);
    }
}
