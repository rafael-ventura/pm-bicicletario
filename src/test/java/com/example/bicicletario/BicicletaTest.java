package com.example.bicicletario;

import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BicicletaTest {

    @Test
    void testGettersAndSetters() {
        Bicicleta bicicleta = new Bicicleta();

        bicicleta.setId(1);
        assertEquals(1, bicicleta.getId());

        bicicleta.setMarca("Caloi");
        assertEquals("Caloi", bicicleta.getMarca());

        bicicleta.setModelo("Mountain Bike");
        assertEquals("Mountain Bike", bicicleta.getModelo());

        bicicleta.setAno("2021");
        assertEquals("2021", bicicleta.getAno());

        bicicleta.setNumero(123);
        assertEquals(123, bicicleta.getNumero());

        bicicleta.setStatus(StatusCiclista.DISPONIVEL);
        assertEquals(StatusCiclista.DISPONIVEL, bicicleta.getStatus());
    }

    @Test
    void testConstructorWithArgs() {
        Bicicleta bicicleta = new Bicicleta("Caloi", "Mountain Bike", "2021", 123, StatusCiclista.DISPONIVEL);

        assertEquals("Caloi", bicicleta.getMarca());
        assertEquals("Mountain Bike", bicicleta.getModelo());
        assertEquals("2021", bicicleta.getAno());
        assertEquals(123, bicicleta.getNumero());
        assertEquals(StatusCiclista.DISPONIVEL, bicicleta.getStatus());
    }

    @Test
    void testDefaultConstructor() {
        Bicicleta bicicleta = new Bicicleta();
        assertNull(bicicleta.getMarca());
        assertNull(bicicleta.getModelo());
        assertNull(bicicleta.getAno());
        assertEquals(0, bicicleta.getNumero());
        assertNull(bicicleta.getStatus());
    }

    @Test
    void shouldReturnBicicleta() {
        Bicicleta bicicleta = new Bicicleta("marca", "modelo", "2021", 1, StatusCiclista.DISPONIVEL);
        assertEquals("marca", bicicleta.getMarca());
        assertEquals("modelo", bicicleta.getModelo());
        assertEquals("2021", bicicleta.getAno());
        assertEquals(1, bicicleta.getNumero());
        assertEquals(StatusCiclista.DISPONIVEL, bicicleta.getStatus());
    }

}
