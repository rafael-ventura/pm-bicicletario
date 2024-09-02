package com.example.bicicletario.Integracao.Controllers;

import com.example.bicicletario.bicicletario.application.exceptions.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.BicicletaService;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(bicicletaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
        when(bicicletaService.listarBicicletas()).thenThrow(new RuntimeException("Erro ao listar bicicletas"));

        mockMvc.perform(get("/api/bicicleta"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao listar bicicletas\"}"));
    }

    @Test
    void cadastrarBicicleta() throws Exception {
        NovaBicicletaDTO dto = new NovaBicicletaDTO();
        dto.setMarca("marca");
        dto.setModelo("modelo");
        dto.setAno("2021");

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");
        bicicleta.setAno("2021");
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaService.cadastrarBicicleta(any(NovaBicicletaDTO.class))).thenReturn(bicicleta);

        mockMvc.perform(post("/api/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(bicicleta)));
    }

    @Test
    void cadastrarBicicleta_ThrowsInvalidDataException() throws Exception {
        doThrow(new InvalidDataException("Dados inválidos")).when(bicicletaService).cadastrarBicicleta(any(NovaBicicletaDTO.class));

        mockMvc.perform(post("/api/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
    }

    @Test
    void cadastrarBicicleta_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao criar bicicleta")).when(bicicletaService).cadastrarBicicleta(any(NovaBicicletaDTO.class));

        mockMvc.perform(post("/api/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao criar bicicleta\"}"));
    }

    @Test
    void obterBicicleta() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");

        when(bicicletaService.obterBicicletaPorId(1)).thenReturn(bicicleta);

        mockMvc.perform(get("/api/bicicleta/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"marca\":\"marca\",\"modelo\":\"modelo\"}"));
    }

    @Test
    void obterBicicleta_ThrowsResourceNotFoundException() throws Exception {
        when(bicicletaService.obterBicicletaPorId(1)).thenThrow(new ResourceNotFoundException("Bicicleta não encontrada"));

        mockMvc.perform(get("/api/bicicleta/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Bicicleta não encontrada\"}"));
    }

    @Test
    void obterBicicleta_ThrowsException() throws Exception {
        when(bicicletaService.obterBicicletaPorId(1)).thenThrow(new RuntimeException("Erro ao obter bicicleta"));

        mockMvc.perform(get("/api/bicicleta/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao obter bicicleta\"}"));
    }

    @Test
    void atualizarBicicleta() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");

        when(bicicletaService.atualizarBicicleta(any(Integer.class), any(NovaBicicletaDTO.class))).thenReturn(bicicleta);

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"marca\":\"marca\",\"modelo\":\"modelo\"}"));
    }

    @Test
    void atualizarBicicleta_ThrowsInvalidDataException() throws Exception {
        doThrow(new InvalidDataException("Dados inválidos")).when(bicicletaService).atualizarBicicleta(any(Integer.class), any(NovaBicicletaDTO.class));

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
    }

    @Test
    void atualizarBicicleta_ThrowsResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Bicicleta não encontrada")).when(bicicletaService).atualizarBicicleta(any(Integer.class), any(NovaBicicletaDTO.class));

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Bicicleta não encontrada\"}"));
    }

    @Test
    void atualizarBicicleta_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao editar bicicleta")).when(bicicletaService).atualizarBicicleta(any(Integer.class), any(NovaBicicletaDTO.class));

        mockMvc.perform(put("/api/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovaBicicletaDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao editar bicicleta\"}"));
    }

    @Test
    void excluirBicicleta() throws Exception {
        mockMvc.perform(delete("/api/bicicleta/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bicicleta removida"));
    }

    @Test
    void excluirBicicleta_ThrowsResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Bicicleta não encontrada")).when(bicicletaService).excluirBicicleta(any(Integer.class));

        mockMvc.perform(delete("/api/bicicleta/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Bicicleta não encontrada\"}"));
    }

    @Test
    void excluirBicicleta_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao remover bicicleta")).when(bicicletaService).excluirBicicleta(any(Integer.class));

        mockMvc.perform(delete("/api/bicicleta/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao remover bicicleta\"}"));
    }

    @Test
    void integrarBicicletaNaRede() throws Exception {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Dados cadastrados"));
    }

    @Test
    void integrarBicicletaNaRede_ThrowsInvalidDataException() throws Exception {
        doThrow(new InvalidDataException("Status da bicicleta inválido")).when(bicicletaService).integrarBicicletaNaRede(any(IntegrarBicicletaNaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IntegrarBicicletaNaRedeDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Status da bicicleta inválido\"}"));
    }

    @Test
    void integrarBicicletaNaRede_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao integrar bicicleta na rede")).when(bicicletaService).integrarBicicletaNaRede(any(IntegrarBicicletaNaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IntegrarBicicletaNaRedeDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao integrar bicicleta na rede\"}"));
    }

    @Test
    void retirarBicicletaDaRede() throws Exception {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Dados cadastrados"));
    }

    @Test
    void retirarBicicletaDaRede_ThrowsInvalidDataException() throws Exception {
        doThrow(new InvalidDataException("Status da bicicleta inválido")).when(bicicletaService).retirarBicicletaDaRede(any(RetirarBicicletaDaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RetirarBicicletaDaRedeDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Status da bicicleta inválido\"}"));
    }

    @Test
    void retirarBicicletaDaRede_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao retirar bicicleta da rede")).when(bicicletaService).retirarBicicletaDaRede(any(RetirarBicicletaDaRedeDTO.class));

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RetirarBicicletaDaRedeDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao retirar bicicleta da rede\"}"));
    }
}
