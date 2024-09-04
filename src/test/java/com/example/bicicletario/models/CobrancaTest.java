package com.example.bicicletario.models;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CobrancaTest {

    @Test
    void testCobranca() {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1);
        cobranca.setStatus("Concluída");
        cobranca.setHoraSolicitacao("2024-09-03T14:30:00");
        cobranca.setHoraFinalizacao("2024-09-03T14:45:00");
        cobranca.setValor(50.0);
        cobranca.setCiclista(101);

        assertEquals(1, cobranca.getId());
        assertEquals("Concluída", cobranca.getStatus());
        assertEquals("2024-09-03T14:30:00", cobranca.getHoraSolicitacao());
        assertEquals("2024-09-03T14:45:00", cobranca.getHoraFinalizacao());
        assertEquals(50.0, cobranca.getValor());
        assertEquals(101, cobranca.getCiclista());
    }
}
