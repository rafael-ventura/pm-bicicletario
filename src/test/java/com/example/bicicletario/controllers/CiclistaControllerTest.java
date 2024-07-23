package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.web.CiclistaController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CiclistaControllerTest {

    @InjectMocks
    private CiclistaController ciclistaController;

    @Mock
    private CiclistaService ciclistaService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(ciclistaController).build();
        this.objectMapper = new ObjectMapper();
    }


    @Test
    void criarCiclista() throws Exception {
        NovoCiclistaDTO dto = new NovoCiclistaDTO();
        dto.setNome("João Silva");
        dto.setCpf("123.456.789-00");
        dto.setEmail("joao.silva@example.com");

        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("João Silva");

        when(ciclistaService.cadastrarCiclista(any(NovoCiclistaRequestDTO.class))).thenReturn(ciclista);

        mockMvc.perform(post("/api/ciclista")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(ciclista)));
    }

    @Test
    void criarCiclista_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Dados inválidos")).when(ciclistaService).cadastrarCiclista(any(NovoCiclistaRequestDTO.class));

        mockMvc.perform(post("/api/ciclista")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Dados inválidos'}"));
    }

    @Test
    void criarCiclista_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao criar ciclista")).when(ciclistaService).cadastrarCiclista(any(NovoCiclistaRequestDTO.class));

        mockMvc.perform(post("/api/ciclista")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao criar ciclista'}"));
    }

    @Test
    void obterCiclista() throws Exception {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("João Silva");

        when(ciclistaService.obterCiclista(1)).thenReturn(Optional.of(ciclista));

        mockMvc.perform(get("/api/ciclista/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'nome':'João Silva'}"));
    }

    @Test
    void obterCiclista_ThrowsNoSuchElementException() throws Exception {
        when(ciclistaService.obterCiclista(1)).thenThrow(new NoSuchElementException("Ciclista não encontrado"));

        mockMvc.perform(get("/api/ciclista/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Ciclista não encontrado'}"));
    }

    @Test
    void obterCiclista_ThrowsException() throws Exception {
        when(ciclistaService.obterCiclista(1)).thenThrow(new RuntimeException("Erro ao obter ciclista"));

        mockMvc.perform(get("/api/ciclista/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao obter ciclista'}"));
    }

    @Test
    void editarCiclista() throws Exception {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("João Silva");

        when(ciclistaService.alterarCiclista(any(Integer.class), any(NovoCiclistaDTO.class))).thenReturn(ciclista);

        mockMvc.perform(put("/api/ciclista/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'nome':'João Silva'}"));
    }

    @Test
    void editarCiclista_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Dados inválidos")).when(ciclistaService).alterarCiclista(any(Integer.class), any(NovoCiclistaDTO.class));

        mockMvc.perform(put("/api/ciclista/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Dados inválidos'}"));
    }

    @Test
    void editarCiclista_ThrowsNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException("Ciclista não encontrado")).when(ciclistaService).alterarCiclista(any(Integer.class), any(NovoCiclistaDTO.class));

        mockMvc.perform(put("/api/ciclista/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Ciclista não encontrado'}"));
    }

    @Test
    void editarCiclista_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao editar ciclista")).when(ciclistaService).alterarCiclista(any(Integer.class), any(NovoCiclistaDTO.class));

        mockMvc.perform(put("/api/ciclista/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao editar ciclista'}"));
    }
}
