package com.example.bicicletario.dto;

import org.junit.jupiter.api.Test;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NovaTrancaDtoTest {

    @Test
    public void testGettersAndSetters() {
        NovaTrancaDTO novaTrancaDto = new NovaTrancaDTO();

        novaTrancaDto.setLocalizacao("Rio de Janeiro");
        novaTrancaDto.setStatus("Disponível");
        novaTrancaDto.setAnoDeFabricacao("2022");
        novaTrancaDto.setModelo("Mountain Bike");
        novaTrancaDto.setNumero(101);

        assertEquals("Rio de Janeiro", novaTrancaDto.getLocalizacao());
        assertEquals("Disponível", novaTrancaDto.getStatus());
        assertEquals("2022", novaTrancaDto.getAnoDeFabricacao());
        assertEquals("Mountain Bike", novaTrancaDto.getModelo());
        assertEquals(101, novaTrancaDto.getNumero());
    }
}
