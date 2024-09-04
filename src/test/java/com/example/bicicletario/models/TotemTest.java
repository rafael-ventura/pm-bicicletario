package com.example.bicicletario.models;

import com.example.bicicletario.bicicletario.domain.Totem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotemTest {

    @Test
    void testTotem() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Centro, Rio de Janeiro");
        totem.setDescricao("Totem de alta capacidade");

        assertEquals(1L, totem.getId());
        assertEquals("Centro, Rio de Janeiro", totem.getLocalizacao());
        assertEquals("Totem de alta capacidade", totem.getDescricao());
    }
}
