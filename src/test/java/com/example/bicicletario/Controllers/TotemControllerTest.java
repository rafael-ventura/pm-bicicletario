package com.example.bicicletario.Controllers;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.web.TotemController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TotemControllerTest {

    @InjectMocks
    private TotemController totemController;

    @Mock
    private TotemService totemService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(totemController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void listarTotens() throws Exception {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemService.listarTotens()).thenReturn(List.of(totem));

        mockMvc.perform(get("/api/totem"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(totem))));
    }

    @Test
    public void listarTotens_ThrowsException() throws Exception {
        when(totemService.listarTotens()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/totem"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void cadastrarTotem() throws Exception {
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        novoTotem.setLocalizacao("Localizacao");
        novoTotem.setDescricao("Descricao");

        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemService.cadastrarTotem(any(NovoTotemDTO.class))).thenReturn(totem);

        mockMvc.perform(post("/api/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoTotem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(totem)));
    }

    @Test
    public void cadastrarTotem_ThrowsIllegalArgumentException() throws Exception {
        when(totemService.cadastrarTotem(any(NovoTotemDTO.class))).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(post("/api/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoTotemDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Error"));
    }

    @Test
    public void editarTotem() throws Exception {
        NovoTotemDTO totem = new NovoTotemDTO();
        totem.setLocalizacao("Nova Localizacao");
        totem.setDescricao("Nova Descricao");

        when(totemService.editarTotem(any(Long.class), any(NovoTotemDTO.class))).thenReturn(new Totem());

        mockMvc.perform(put("/api/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void editarTotem_ThrowsIllegalArgumentException() throws Exception {
        when(totemService.editarTotem(any(Long.class), any(NovoTotemDTO.class))).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(put("/api/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Totem())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Error"));
    }

    @Test
    public void removerTotem() throws Exception {
        mockMvc.perform(delete("/api/totem/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Totem removido"));
    }

    @Test
    public void removerTotem_ThrowsIllegalArgumentException() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Error")).when(totemService).removerTotem(any(Long.class));

        mockMvc.perform(delete("/api/totem/1"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Error"));
    }

    @Test
    public void listarTrancas() throws Exception {
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(StatusTranca.OCUPADA);

        when(totemService.listarTrancas(any(Long.class))).thenReturn(List.of(tranca));

        mockMvc.perform(get("/api/totem/1/trancas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(tranca))));
    }

    @Test
    public void listarTrancas_ThrowsException() throws Exception {
        when(totemService.listarTrancas(any(Long.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/totem/1/trancas"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void listarBicicletas() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(1);
        bicicleta.setModelo("Modelo");
        bicicleta.setAno("2021");
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);

        when(totemService.listarBicicletas(any(Long.class))).thenReturn(List.of(bicicleta));

        mockMvc.perform(get("/api/totem/1/bicicletas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bicicleta))));
    }

    @Test
    public void listarBicicletas_ThrowsException() throws Exception {
        when(totemService.listarBicicletas(any(Long.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/totem/1/bicicletas"))
                .andExpect(status().isInternalServerError());
    }

}
