package com.example.bicicletario.integracao;

import com.example.bicicletario.bicicletario.application.ValidaCartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.web.ValidaCartaoDeCreditoController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ValidaCartaoDeCreditoControllerTest {

    @InjectMocks
    private ValidaCartaoDeCreditoController validaCartaoDeCreditoController;

    @Mock
    private ValidaCartaoDeCreditoService validaCartaoDeCreditoService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(validaCartaoDeCreditoController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void validarCartaoDeCreditoComSucesso() throws Exception {
        NovoCartaoDeCreditoDTO cartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();

        when(validaCartaoDeCreditoService.validarCartao(any(NovoCartaoDeCreditoDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/validaCartaoDeCredito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDeCreditoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Dados atualizados"));
    }

    @Test
    void validarCartaoDeCreditoComDadosInvalidos() throws Exception {
        NovoCartaoDeCreditoDTO cartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();

        when(validaCartaoDeCreditoService.validarCartao(any(NovoCartaoDeCreditoDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/validaCartaoDeCredito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDeCreditoDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("422", "Dados Inválidos"))));
    }
}
