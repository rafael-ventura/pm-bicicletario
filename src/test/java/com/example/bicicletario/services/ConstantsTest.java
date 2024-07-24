package com.example.bicicletario.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bicicletario.bicicletario.application.Constants;
import org.junit.jupiter.api.Test;

public class ConstantsTest {

    @Test
    public void testConstants() {
        assertEquals("Ciclista não encontrado com o ID: ", Constants.CICLISTA_NAO_ENCONTRADO);
    }
}
