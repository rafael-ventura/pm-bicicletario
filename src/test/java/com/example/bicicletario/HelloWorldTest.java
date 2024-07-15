package com.example.bicicletario;

import com.example.bicicletario.bicicletario.domain.HelloWorld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {

    @Test
    void testGettersAndSetters() {
        HelloWorld helloWorld = new HelloWorld();

        helloWorld.setNome("Hello World - endpoint");
        assertEquals("Hello World - endpoint", helloWorld.getNome());
    }

    @Test
    void testConstructorWithArgs() {
        HelloWorld helloWorld = new HelloWorld();

        assertEquals("Hello World - endpoint", helloWorld.getNome());
    }

    @Test
    void testDefaultConstructor() {
        HelloWorld helloWorld = new HelloWorld();
    }

    @Test
    void shouldReturnHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        assertEquals("Hello World - endpoint", helloWorld.getNome());
    }
}
