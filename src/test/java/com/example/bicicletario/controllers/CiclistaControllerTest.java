package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.exception.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.web.CiclistaController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(ciclistaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"nome\":\"João Silva\"}"));
    }

    @Test
    void criarCiclista_ThrowsInvalidDataException() throws Exception {
        doThrow(new InvalidDataException("Dados inválidos")).when(ciclistaService).cadastrarCiclista(any(NovoCiclistaRequestDTO.class));

        mockMvc.perform(post("/api/ciclista")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
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
    void obterCiclista_ThrowsResourceNotFoundException() throws Exception {
        when(ciclistaService.obterCiclista(1)).thenThrow(new ResourceNotFoundException("Ciclista não encontrado"));

        mockMvc.perform(get("/api/ciclista/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Ciclista não encontrado'}"));
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
    void editarCiclista_ThrowsResourceNotFoundException() throws Exception {
        // Configura o comportamento esperado do serviço
        doThrow(new ResourceNotFoundException("Ciclista não encontrado")).when(ciclistaService).alterarCiclista(any(Integer.class), any(NovoCiclistaDTO.class));

        // Realiza a requisição e verifica a resposta
        mockMvc.perform(put("/api/ciclista/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
                .andExpect(status().isNotFound()) // Verifica o status 404
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica o tipo de conteúdo
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Ciclista não encontrado\"}")); // Verifica o conteúdo JSON da resposta
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
