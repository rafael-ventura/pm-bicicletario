package com.example.bicicletario.dto;

import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrarBicicletaNaRedeDTOTest {

    @Test
    public void testGettersAndSetters() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();

        dto.setIdBicicleta(1L);
        dto.setIdTranca(2L);
        dto.setIdFuncionario(3L);

        assertEquals(1L, dto.getIdBicicleta());
        assertEquals(2L, dto.getIdTranca());
        assertEquals(3L, dto.getIdFuncionario());
    }
}
