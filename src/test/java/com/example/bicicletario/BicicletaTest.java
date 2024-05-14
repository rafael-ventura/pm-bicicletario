package com.example.bicicletario;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaTest {

    @Test
    public void testGettersAndSetters() {
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

        bicicleta.setStatus(Status.DISPONIVEL);
        assertEquals(Status.DISPONIVEL, bicicleta.getStatus());
    }

    @Test
    public void testConstructorWithArgs() {
        Bicicleta bicicleta = new Bicicleta("Caloi", "Mountain Bike", "2021", 123, Status.DISPONIVEL);

        assertEquals("Caloi", bicicleta.getMarca());
        assertEquals("Mountain Bike", bicicleta.getModelo());
        assertEquals("2021", bicicleta.getAno());
        assertEquals(123, bicicleta.getNumero());
        assertEquals(Status.DISPONIVEL, bicicleta.getStatus());
    }

    @Test
    public void testDefaultConstructor() {
        Bicicleta bicicleta = new Bicicleta();

        assertNull(bicicleta.getMarca());
        assertNull(bicicleta.getModelo());
        assertNull(bicicleta.getAno());
        assertEquals(0, bicicleta.getNumero());
        assertNull(bicicleta.getStatus());
    }

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
