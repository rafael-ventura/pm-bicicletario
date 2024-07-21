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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);

        when(bicicletaService.listarBicicletas()).thenReturn(List.of(bicicleta));

        mockMvc.perform(get("/api/bicicletas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'marca':'marca','modelo':'modelo','ano':'2021','numero':1,'status':'DISPONIVEL'}]"));
    }

    @Test
    void listarBicicletas_ThrowsException() throws Exception {
        when(bicicletaService.listarBicicletas()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/bicicletas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao listar bicicletas'}"));
    }

    @Test
    void criarBicicleta_ThrowsException() throws Exception {
        when(bicicletaService.criarBicicleta(any(NovaBicicletaDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/bicicletas")
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
}
