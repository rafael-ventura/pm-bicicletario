package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.web.CartaoDeCreditoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CartaoDeCreditoControllerTest {

    @Mock
    private CartaoDeCreditoService cartaoDeCreditoService;

    @InjectMocks
    private CartaoDeCreditoController cartaoDeCreditoController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoDeCreditoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void obterCartaoDeCredito() throws Exception {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(1);
        cartaoDeCredito.setNumero("1234567890123456");

        when(cartaoDeCreditoService.obterCartaoDeCredito(anyInt())).thenReturn(cartaoDeCredito);

        mockMvc.perform(get("/api/cartaoDeCredito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numero").value("1234567890123456"));
    }

    @Test
    void alterarCartaoDeCredito() throws Exception {
        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDeCreditoDTO.setNomeTitular("Nome");
        novoCartaoDeCreditoDTO.setNumero("1234567890123456");
        novoCartaoDeCreditoDTO.setValidade("2025-12-31");
        novoCartaoDeCreditoDTO.setCvv("123");

        doNothing().when(cartaoDeCreditoService).alterarCartaoDeCredito(anyInt(), any(NovoCartaoDeCreditoDTO.class));

        mockMvc.perform(put("/api/cartaoDeCredito/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoCartaoDeCreditoDTO)))
                .andExpect(status().isOk());
    }
}