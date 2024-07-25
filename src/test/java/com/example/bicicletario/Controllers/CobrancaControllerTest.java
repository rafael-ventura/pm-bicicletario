package com.example.bicicletario.Controllers;

import com.example.bicicletario.bicicletario.application.CobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.web.CobrancaController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CobrancaControllerTest {

    @InjectMocks
    private CobrancaController cobrancaController;

    @Mock
    private CobrancaService cobrancaService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cobrancaController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void realizarCobrancaComSucesso() throws Exception {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();
        Cobranca cobranca = new Cobranca();

        when(cobrancaService.realizarCobranca(any(NovoCobrancaDTO.class))).thenReturn(cobranca);

        mockMvc.perform(post("/api/cobranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCobrancaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(cobranca)));
    }

    @Test
    void realizarCobrancaComErro() throws Exception {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();

        when(cobrancaService.realizarCobranca(any(NovoCobrancaDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/cobranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCobrancaDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("422", "Dados Inválidos"))));
    }

    @Test
    void obterCobrancaComSucesso() throws Exception {
        int idCobranca = 1;
        Cobranca cobranca = new Cobranca();

        when(cobrancaService.obterCobrancaPorId(idCobranca)).thenReturn(cobranca);

        mockMvc.perform(get("/api/cobranca/{idCobranca}", idCobranca))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(cobranca)));
    }

    @Test
    void obterCobrancaNaoEncontrada() throws Exception {
        int idCobranca = 1;

        when(cobrancaService.obterCobrancaPorId(idCobranca)).thenReturn(null);

        mockMvc.perform(get("/api/cobranca/{idCobranca}", idCobranca))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("404", "Cobrança não encontrada"))));
    }
}
