package com.example.bicicletario;

import org.junit.jupiter.api.Test;

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
        helloWorld.setNome("Hello World - endpoint");
        assertEquals("Hello World - endpoint", helloWorld.getNome());
    }

    @Test
    void testDefaultConstructor() {
        HelloWorld helloWorld = new HelloWorld();
    }

    @Test
    void shouldReturnHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setNome("Hello World - endpoint");
        assertEquals("Hello World - endpoint", helloWorld.getNome());
    }
}
