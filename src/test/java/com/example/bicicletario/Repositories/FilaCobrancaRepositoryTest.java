package com.example.bicicletario.Repositories;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.infraestructure.FilaCobrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class FilaCobrancaRepositoryTest {

    private FilaCobrancaRepository filaCobrancaRepository;

    @BeforeEach
    void setUp() {
        filaCobrancaRepository = new FilaCobrancaRepository();
    }

    @Test
    void testAdicionarNaFila() {
        Cobranca cobranca = new Cobranca();
        filaCobrancaRepository.adicionarNaFila(cobranca);

        Queue<Cobranca> fila = filaCobrancaRepository.obterFila();
        assertEquals(1, fila.size());
        assertTrue(fila.contains(cobranca));
    }

    @Test
    void testRemoverDaFila() {
        Cobranca cobranca = new Cobranca();
        filaCobrancaRepository.adicionarNaFila(cobranca);

        Cobranca removida = filaCobrancaRepository.removerDaFila();
        assertEquals(cobranca, removida);

        Queue<Cobranca> fila = filaCobrancaRepository.obterFila();
        assertTrue(fila.isEmpty());
    }

    @Test
    void testObterFila() {
        Cobranca cobranca1 = new Cobranca();
        Cobranca cobranca2 = new Cobranca();
        filaCobrancaRepository.adicionarNaFila(cobranca1);
        filaCobrancaRepository.adicionarNaFila(cobranca2);

        Queue<Cobranca> fila = filaCobrancaRepository.obterFila();
        assertEquals(2, fila.size());
        assertTrue(fila.contains(cobranca1));
        assertTrue(fila.contains(cobranca2));
    }

    @Test
    void testIsEmpty() {
        assertTrue(filaCobrancaRepository.isEmpty());

        Cobranca cobranca = new Cobranca();
        filaCobrancaRepository.adicionarNaFila(cobranca);

        assertFalse(filaCobrancaRepository.isEmpty());
    }
}
