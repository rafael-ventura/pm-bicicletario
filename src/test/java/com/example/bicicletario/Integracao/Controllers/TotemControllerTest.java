package com.example.bicicletario.Integracao.Controllers;

import com.example.bicicletario.bicicletario.application.services.TotemService;
import com.example.bicicletario.bicicletario.application.exceptions.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.web.TotemController;
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

class TotemControllerTest {

    @InjectMocks
    private TotemController totemController;

    @Mock
    private TotemService totemService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(totemController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void listarTotens() throws Exception {
        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemService.listarTodosTotens()).thenReturn(List.of(totem));

        mockMvc.perform(get("/api/totem"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(totem))));
    }

    @Test
    void listarTotens_ThrowsException() throws Exception {
        when(totemService.listarTodosTotens()).thenThrow(new RuntimeException("Erro ao listar totens"));

        mockMvc.perform(get("/api/totem"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao listar totens\"}"));
    }

    @Test
    void cadastrarTotem() throws Exception {
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        novoTotem.setLocalizacao("Localizacao");
        novoTotem.setDescricao("Descricao");

        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemService.cadastrarNovoTotem(any(NovoTotemDTO.class))).thenReturn(totem);

        mockMvc.perform(post("/api/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoTotem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(totem)));
    }

    @Test
    void cadastrarTotem_ThrowsInvalidDataException() throws Exception {
        when(totemService.cadastrarNovoTotem(any(NovoTotemDTO.class))).thenThrow(new InvalidDataException("Dados inválidos"));

        mockMvc.perform(post("/api/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoTotemDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
    }

    @Test
    void cadastrarTotem_ThrowsException() throws Exception {
        when(totemService.cadastrarNovoTotem(any(NovoTotemDTO.class))).thenThrow(new RuntimeException("Erro ao criar totem"));

        mockMvc.perform(post("/api/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoTotemDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao criar totem\"}"));
    }

    @Test
    void editarTotem() throws Exception {
        NovoTotemDTO totemDTO = new NovoTotemDTO();
        totemDTO.setLocalizacao("Nova Localizacao");
        totemDTO.setDescricao("Nova Descricao");

        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Nova Localizacao");
        totem.setDescricao("Nova Descricao");

        when(totemService.atualizarTotem(any(Integer.class), any(NovoTotemDTO.class))).thenReturn(totem);

        mockMvc.perform(put("/api/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totemDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(totem)));
    }

    @Test
    void editarTotem_ThrowsInvalidDataException() throws Exception {
        doThrow(new InvalidDataException("Dados inválidos")).when(totemService).atualizarTotem(any(Integer.class), any(NovoTotemDTO.class));

        mockMvc.perform(put("/api/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoTotemDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
    }

    @Test
    void editarTotem_ThrowsResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Totem não encontrado")).when(totemService).atualizarTotem(any(Integer.class), any(NovoTotemDTO.class));

        mockMvc.perform(put("/api/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoTotemDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Totem não encontrado\"}"));
    }

    @Test
    void editarTotem_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao editar totem")).when(totemService).atualizarTotem(any(Integer.class), any(NovoTotemDTO.class));

        mockMvc.perform(put("/api/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoTotemDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao editar totem\"}"));
    }

    @Test
    void removerTotem() throws Exception {
        mockMvc.perform(delete("/api/totem/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Totem removido"));
    }

    @Test
    void removerTotem_ThrowsResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Totem não encontrado")).when(totemService).excluirTotem(any(Integer.class));

        mockMvc.perform(delete("/api/totem/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Totem não encontrado\"}"));
    }

    @Test
    void removerTotem_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao remover totem")).when(totemService).excluirTotem(any(Integer.class));

        mockMvc.perform(delete("/api/totem/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao remover totem\"}"));
    }

    @Test
    void listarTrancas() throws Exception {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        when(totemService.listarTrancasPorTotem(any(Integer.class))).thenReturn(List.of(tranca));

        mockMvc.perform(get("/api/totem/1/trancas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(tranca))));
    }

    @Test
    void listarTrancas_ThrowsException() throws Exception {
        when(totemService.listarTrancasPorTotem(any(Integer.class))).thenThrow(new RuntimeException("Erro ao listar trancas do totem"));

        mockMvc.perform(get("/api/totem/1/trancas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao listar trancas do totem\"}"));
    }

    @Test
    void listarBicicletas() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setNumero(1);
        bicicleta.setModelo("Modelo");
        bicicleta.setAno("2021");
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(totemService.listarBicicletasPorTotem(any(Integer.class))).thenReturn(List.of(bicicleta));

        mockMvc.perform(get("/api/totem/1/bicicletas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bicicleta))));
    }

    @Test
    void listarBicicletas_ThrowsException() throws Exception {
        when(totemService.listarBicicletasPorTotem(any(Integer.class))).thenThrow(new RuntimeException("Erro ao listar bicicletas do totem"));

        mockMvc.perform(get("/api/totem/1/bicicletas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao listar bicicletas do totem\"}"));
    }
}
