package com.example.bicicletario.Controllers;

import com.example.bicicletario.bicicletario.application.BicicletaService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrancaControllerTest {

    @Mock
    private TrancaService trancaService;

    @Mock
    private BicicletaService bicicletaService;

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
    void integrarNaRede_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(trancaService).integrarNaRede(any(IntegrarBicicletaNaRedeDTO.class));

        mockMvc.perform(post("/api/tranca/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Error"));
    }

    @Test
    void integrarNaRede_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(trancaService).integrarNaRede(any(IntegrarBicicletaNaRedeDTO.class));

        mockMvc.perform(post("/api/tranca/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void retirarDaRede_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(trancaService).retirarDaRede(any(RetirarTrancaDaRedeDTO.class));

        mockMvc.perform(post("/api/tranca/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Error"));
    }

    @Test
    void retirarDaRede_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(trancaService).retirarDaRede(any(RetirarTrancaDaRedeDTO.class));

        mockMvc.perform(post("/api/tranca/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void listarTrancas_ThrowsException() throws Exception {
        when(trancaService.listarTrancas()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/tranca"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void cadastrarTranca_ThrowsException() throws Exception {
        when(trancaService.cadastrarTranca(any(TrancaDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/tranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void obterTranca_ThrowsException() throws Exception {
        when(trancaService.obterTranca(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/tranca/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void editarTranca_ThrowsException() throws Exception {
        when(trancaService.editarTranca(any(Long.class), any(TrancaDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/tranca/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
    }

    @Test
    void removerTranca() throws Exception {
        mockMvc.perform(delete("/api/tranca/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tranca removida"));
    }

    @Test
    void removerTranca_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(trancaService).removerTranca(any(Long.class));

        mockMvc.perform(delete("/api/tranca/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void obterBicicletaNaTranca_ThrowsException() throws Exception {
        // Configura o Mockito para lançar uma exceção quando o método 'obterBicicletaNaTranca' for chamado
        when(trancaService.obterBicicletaNaTranca(any(Long.class))).thenThrow(new RuntimeException("Error"));

        // Executa a requisição e verifica se o status de resposta é 'InternalServerError' e se a mensagem é "Error"
        mockMvc.perform(get("/api/tranca/1/bicicleta"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void trancarTranca_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(trancaService).trancarTranca(any(Long.class), any(Long.class));

        mockMvc.perform(post("/api/tranca/1/trancar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
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
    void destrancarTranca_ThrowsException() throws Exception {
        // Configura o Mockito para lançar uma exceção quando o método 'destrancarTranca' for chamado
        doThrow(new RuntimeException("Error")).when(trancaService).destrancarTranca(any(Long.class), any(Long.class));

        // Executa a requisição e verifica se o status de resposta é 'InternalServerError' e se a mensagem é "Error"
        mockMvc.perform(post("/api/tranca/1/destrancar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
    }

    @Test
    void alterarStatusTranca() throws Exception {
        mockMvc.perform(post("/api/tranca/1/status/acao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Ação bem sucedida"));
    }

    @Test
    void alterarStatusTranca_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(trancaService).alterarStatusTranca(any(Long.class), any(String.class));

        mockMvc.perform(post("/api/tranca/1/status/acao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error"));
    }
}
