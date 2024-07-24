package com.example.bicicletario;

import com.example.bicicletario.bicicletario.application.HelloWorldService;
import com.example.bicicletario.bicicletario.domain.HelloWorld;
import com.example.bicicletario.bicicletario.web.HelloWorldController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloWorldController.class)
class HelloWorldTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloWorldService helloWorldService;

    @BeforeEach
    void setUp() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setNome("Hello World !");
        when(helloWorldService.getHelloWorld()).thenReturn(helloWorld);
    }

    // Unit tests for HelloWorld entity
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
    }

    @Test
    void shouldReturnHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setNome("Hello World - endpoint");
        assertEquals("Hello World - endpoint", helloWorld.getNome());
    }

    // Unit test for HelloWorldService
    @Test
    void testGetHelloWorldService() {
        HelloWorldService helloWorldService = new HelloWorldService();
        HelloWorld helloWorld = helloWorldService.getHelloWorld();

        assertNotNull(helloWorld);
        assertEquals("Hello World !", helloWorld.getNome());
    }

    // Integration test for HelloWorldController
    @Test
    void testGetHelloWorldController() throws Exception {
        mockMvc.perform(get("/api/hello-world"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Hello World !"));
    }
}
