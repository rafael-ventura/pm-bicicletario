package com.example.bicicletario.dto;

import com.example.bicicletario.bicicletario.domain.dto.IntegrarTrancaNaRedeDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrarTrancaNaRedeDTOTest {

    @Test
    public void testGettersAndSetters() {
        IntegrarTrancaNaRedeDTO integrarTrancaNaRedeDTO = new IntegrarTrancaNaRedeDTO();

        integrarTrancaNaRedeDTO.setIdTranca(1L);
        integrarTrancaNaRedeDTO.setIdBicicleta(1L);
        integrarTrancaNaRedeDTO.setIdFuncionario(1L);

        assertEquals(1L, integrarTrancaNaRedeDTO.getIdTranca());
        assertEquals(1L, integrarTrancaNaRedeDTO.getIdBicicleta());
        assertEquals(1L, integrarTrancaNaRedeDTO.getIdFuncionario());
    }
}
