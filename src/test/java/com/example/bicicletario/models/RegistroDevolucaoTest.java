package com.example.bicicletario.models;

import com.example.bicicletario.bicicletario.domain.RegistroDevolucao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegistroDevolucaoTest {

    @Test
    void testRegistroDevolucao() {
        RegistroDevolucao devolucao = new RegistroDevolucao();
        devolucao.setId(1);
        devolucao.setCiclistaId(101);
        devolucao.setBicicletaId(202);
        devolucao.setTrancaId(303);
        devolucao.setDataHoraDevolucao(LocalDateTime.now());
        devolucao.setDataHoraCobranca(LocalDateTime.now().plusMinutes(30));
        devolucao.setValorExtra(15.50);
        devolucao.setCartaoUsado(12345);
        devolucao.setStatusPagamento("Pago");

        assertEquals(1, devolucao.getId());
        assertEquals(101, devolucao.getCiclistaId());
        assertEquals(202, devolucao.getBicicletaId());
        assertEquals(303, devolucao.getTrancaId());
        assertNotNull(devolucao.getDataHoraDevolucao());
        assertNotNull(devolucao.getDataHoraCobranca());
        assertEquals(15.50, devolucao.getValorExtra());
        assertEquals(12345, devolucao.getCartaoUsado());
        assertEquals("Pago", devolucao.getStatusPagamento());
    }
}
