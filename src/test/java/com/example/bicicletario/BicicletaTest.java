package com.example.bicicletario;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BicicletaTest {

    @Test
    void shouldReturnBicicleta() {
        Bicicleta bicicleta = new Bicicleta("marca", "modelo", "2021", 1, Status.DISPONIVEL);
        assertEquals("marca", bicicleta.getMarca());
        assertEquals("modelo", bicicleta.getModelo());
        assertEquals("2021", bicicleta.getAno());
        assertEquals(1, bicicleta.getNumero());
        assertEquals(Status.DISPONIVEL, bicicleta.getStatus());

    }

    @Test
    void shouldReturnBicicletaEmpty() {
        Bicicleta bicicleta = new Bicicleta();
        assertNull(bicicleta.getMarca());
        assertNull(bicicleta.getModelo());
        assertNull(bicicleta.getAno());
        assertEquals(0, bicicleta.getNumero());
        assertNull(bicicleta.getStatus());
    }

}