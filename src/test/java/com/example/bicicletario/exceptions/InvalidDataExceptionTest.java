package com.example.bicicletario.exceptions;

import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidDataExceptionTest {

    @Test
    void testInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> {
            throw new InvalidDataException("This is an invalid data exception");
        });
    }
}
