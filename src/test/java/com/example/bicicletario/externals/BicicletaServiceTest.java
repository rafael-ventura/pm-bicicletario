package com.example.bicicletario.externals;

import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BicicletaServiceTest {

    @Test
    void testGetBicicletaByTranca() {
        BicicletaService bicicletaService = new BicicletaService();
        Bicicleta bicicleta = bicicletaService.getBicicletaByTranca().orElseThrow(() -> new RuntimeException("Bicicleta não encontrada."));

        assertEquals(1, bicicleta.getId());
        assertEquals("Caloi", bicicleta.getMarca());
        assertEquals("Elite", bicicleta.getModelo());
        assertEquals("2021", bicicleta.getAno());
        assertEquals(1, bicicleta.getNumero());
    }
}
