package com.example.bicicletario.Controllers;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.web.BicicletaController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BicicletaControllerTest {

    @Mock
    private BicicletaService bicicletaService;

    @InjectMocks
    private BicicletaController bicicletaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bicicletaController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void listarBicicletas() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");
        bicicleta.setAno("2021");
        bicicleta.setNumero(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaService.listarBicicletas()).thenReturn(List.of(bicicleta));

        mockMvc.perform(get("/api/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'marca':'marca','modelo':'modelo','ano':'2021','numero':1,'statusBicicleta':'DISPONIVEL'}]"));
    }

    @Test
    void listarBicicletas_ThrowsException() throws Exception {
        when(bicicletaService.listarBicicletas()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/bicicleta"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao listar bicicletas'}"));
    }

    @Test
    void criarBicicleta_ThrowsException() throws Exception {
        when(bicicletaService.criarBicicleta(any(NovaBicicletaDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NOVA\",\"modelo\":\"modelo\",\"localizacao\":\"localizacao\",\"numero\":1,\"modelo\":\"modelo\",\"anoDeFabricacao\":\"2021\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao criar bicicleta'}"));
    }

    @Test
    void integrarNaRede() throws Exception {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Dados cadastrados"));
    }

    @Test
    void integrarNaRede_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(bicicletaService).integrarNaRede(any(IntegrarBicicletaNaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IntegrarBicicletaNaRedeDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Status da bicicleta inválido'}"));
    }

    @Test
    void integrarNaRede_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(bicicletaService).integrarNaRede(any(IntegrarBicicletaNaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IntegrarBicicletaNaRedeDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao integrar bicicleta na rede'}"));
    }

    @Test
    void retirarDaRede() throws Exception {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Dados cadastrados"));
    }

    @Test
    void retirarDaRede_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(bicicletaService).retirarDaRede(any(RetirarBicicletaDaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RetirarBicicletaDaRedeDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Status da bicicleta inválido'}"));
    }

    @Test
    void retirarDaRede_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(bicicletaService).retirarDaRede(any(RetirarBicicletaDaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RetirarBicicletaDaRedeDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao retirar bicicleta da rede'}"));
    }

    @Test
    void obterBicicleta() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");

        when(bicicletaService.obterBicicleta(1L)).thenReturn(bicicleta);

        mockMvc.perform(get("/api/bicicleta/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'marca':'marca','modelo':'modelo'}"));
    }

    @Test
    void obterBicicleta_ThrowsNoSuchElementException() throws Exception {
        when(bicicletaService.obterBicicleta(1L)).thenThrow(new NoSuchElementException("Error"));

        mockMvc.perform(get("/api/bicicleta/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Bicicleta não encontrada'}"));
    }

    @Test
    void obterBicicleta_ThrowsException() throws Exception {
        when(bicicletaService.obterBicicleta(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/bicicleta/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao obter bicicleta'}"));
    }

    @Test
    void editarBicicleta() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");

        when(bicicletaService.editarBicicleta(any(Long.class), any(NovaBicicletaDTO.class))).thenReturn(bicicleta);

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'marca':'marca','modelo':'modelo'}"));
    }

    @Test
    void editarBicicleta_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(bicicletaService).editarBicicleta(any(Long.class), any(NovaBicicletaDTO.class));

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Dados inválidos'}"));
    }

    @Test
    void editarBicicleta_ThrowsNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException("Error")).when(bicicletaService).editarBicicleta(any(Long.class), any(NovaBicicletaDTO.class));

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Bicicleta não encontrada'}"));
    }

    @Test
    void editarBicicleta_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(bicicletaService).editarBicicleta(any(Long.class), any(NovaBicicletaDTO.class));

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao editar bicicleta'}"));
    }

    @Test
    void removerBicicleta() throws Exception {
        mockMvc.perform(delete("/api/bicicleta/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bicicleta removida"));
    }

    @Test
    void removerBicicleta_ThrowsNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException("Error")).when(bicicletaService).removerBicicleta(any(Long.class));

        mockMvc.perform(delete("/api/bicicleta/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Bicicleta não encontrada'}"));
    }

    @Test
    void removerBicicleta_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Error")).when(bicicletaService).removerBicicleta(any(Long.class));

        mockMvc.perform(delete("/api/bicicleta/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao remover bicicleta'}"));
    }
}
