package com.example.bicicletario.Controllers;

import com.example.bicicletario.bicicletario.application.FilaCobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.web.FilaCobrancaController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilaCobrancaControllerTest {

    @InjectMocks
    private FilaCobrancaController filaCobrancaController;

    @Mock
    private FilaCobrancaService filaCobrancaService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(filaCobrancaController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void adicionarNaFilaComSucesso() throws Exception {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();
        Cobranca cobranca = new Cobranca();

        when(filaCobrancaService.adicionarNaFila(any(NovoCobrancaDTO.class))).thenReturn(cobranca);

        mockMvc.perform(post("/api/filaCobranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCobrancaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(cobranca)));
    }

    @Test
    void adicionarNaFilaComErroDeArgumentoIlegal() throws Exception {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();

        when(filaCobrancaService.adicionarNaFila(any(NovoCobrancaDTO.class)))
                .thenThrow(new IllegalArgumentException("Argumento inválido"));

        mockMvc.perform(post("/api/filaCobranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCobrancaDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("422", "Argumento inválido"))));
    }

    @Test
    void adicionarNaFilaComErroInterno() throws Exception {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();

        when(filaCobrancaService.adicionarNaFila(any(NovoCobrancaDTO.class)))
                .thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(post("/api/filaCobranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCobrancaDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("500", "Erro interno"))));
    }

    @Test
    void processarFilaComSucesso() throws Exception {
        Cobranca cobranca = new Cobranca();
        List<Cobranca> cobrancasProcessadas = Collections.singletonList(cobranca);

        when(filaCobrancaService.processarFila()).thenReturn(cobrancasProcessadas);

        mockMvc.perform(post("/api/processaCobrancasEmFila"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(cobrancasProcessadas)));
    }

    @Test
    void processarFilaComErroInterno() throws Exception {
        when(filaCobrancaService.processarFila()).thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(post("/api/processaCobrancasEmFila"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("422", "Erro interno"))));
    }
}
