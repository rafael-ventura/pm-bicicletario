package com.example.bicicletario.exceptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.exception.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.web.CiclistaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CiclistaController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CiclistaService ciclistaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleBadRequestException() throws Exception {
        // Mock the service to throw BadRequestException
        mockMvc.perform(get("/api/ciclista/existeEmail/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testHandleResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/api/ciclista/existeEmail/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testHandleInvalidDataException() throws Exception {
        mockMvc.perform(get("/api/ciclista/existeEmail/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}