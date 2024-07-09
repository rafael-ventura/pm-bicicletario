package com.example.bicicletario.Controllers;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.TrancaDTO;
import com.example.bicicletario.bicicletario.web.TrancaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrancaControllerTest {

    @Mock
    private TrancaService trancaService;

    @InjectMocks
    private TrancaController trancaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(trancaController).build();
    }

    @Test
    void integrarNaRede() throws Exception {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        mockMvc.perform(post("/api/tranca/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dados cadastrados"));
    }

    @Test
    void retirarDaRede() throws Exception {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        mockMvc.perform(post("/api/tranca/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tranca foi retirada com sucesso!"));
    }

    @Test
    void listarTrancas() throws Exception {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaService.listarTrancas()).thenReturn(List.of(tranca));

        mockMvc.perform(get("/api/tranca"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1}]"));
    }

    @Test
    void cadastrarTranca() throws Exception {
        TrancaDTO trancaDTO = new TrancaDTO();
        trancaDTO.setId(1L);
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaService.cadastrarTranca(any(TrancaDTO.class))).thenReturn(tranca);

        mockMvc.perform(post("/api/tranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void obterTranca() throws Exception {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaService.obterTranca(1L)).thenReturn(tranca);

        mockMvc.perform(get("/api/tranca/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void editarTranca() throws Exception {
        TrancaDTO trancaDTO = new TrancaDTO();
        trancaDTO.setId(1L);
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaService.editarTranca(any(Long.class), any(TrancaDTO.class))).thenReturn(tranca);

        mockMvc.perform(put("/api/tranca/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void removerTranca() throws Exception {
        mockMvc.perform(delete("/api/tranca/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tranca removida"));
    }

    @Test
    void obterBicicletaNaTranca() throws Exception {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaService.obterBicicletaNaTranca(1L)).thenReturn(tranca);

        mockMvc.perform(get("/api/tranca/1/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void trancarTranca() throws Exception {
        mockMvc.perform(post("/api/tranca/1/trancar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ação bem sucedida"));
    }

    @Test
    void destrancarTranca() throws Exception {
        mockMvc.perform(post("/api/tranca/1/destrancar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ação bem sucedida"));
    }

    @Test
    void alterarStatusTranca() throws Exception {
        mockMvc.perform(post("/api/tranca/1/status/acao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Ação bem sucedida"));
    }
}
