package com.example.bicicletario.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.bicicletario.bicicletario.exception.BadRequestException;
import org.junit.jupiter.api.Test;

class BadRequestExceptionTest {

    @Test
    void testBadRequestException() {
        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("This is a bad request");
        });
    }
}
