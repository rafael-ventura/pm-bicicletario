package com.example.bicicletario.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.example.bicicletario.bicicletario.domain.dto.BicicletaDTO;

public class BicicletaDTOTest {

    @Test
    public void testGettersAndSetters() {
        BicicletaDTO bicicletaDTO = new BicicletaDTO();
        
        bicicletaDTO.setId(1L);
        bicicletaDTO.setNumero(101);
        bicicletaDTO.setLocalizacao("Rio de Janeiro");
        bicicletaDTO.setAnoDeFabricacao("2022");
        bicicletaDTO.setModelo("Mountain Bike");
        bicicletaDTO.setStatus("Disponível");

        assertEquals(1L, bicicletaDTO.getId());
        assertEquals(101, bicicletaDTO.getNumero());
        assertEquals("Rio de Janeiro", bicicletaDTO.getLocalizacao());
        assertEquals("2022", bicicletaDTO.getAnoDeFabricacao());
        assertEquals("Mountain Bike", bicicletaDTO.getModelo());
        assertEquals("Disponível", bicicletaDTO.getStatus());
    }
}
