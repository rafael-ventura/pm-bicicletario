package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import com.example.bicicletario.bicicletario.web.FuncionarioController;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FuncionarioControllerTest {

    @InjectMocks
    private FuncionarioController funcionarioController;

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void listarFuncionarios() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("João Silva");

        when(funcionarioMapper.toEntityList(funcionarioService.listarFuncionarios())).thenReturn(List.of(funcionario));

        mockMvc.perform(get("/api/funcionario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(funcionario))));
    }

    @Test
    void listarFuncionarios_ThrowsException() throws Exception {
        when(funcionarioService.listarFuncionarios()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/funcionario"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao listar funcionários'}"));
    }

    @Test
    void criarFuncionario() throws Exception {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("João Silva");
        dto.setSenha("senha123");
        dto.setConfirmacaoSenha("senha123");
        dto.setIdade(30);
        dto.setFuncao("Administrador");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("João Silva");

        when(funcionarioMapper.toEntity(funcionarioService.cadastrarFuncionario(any(NovoFuncionarioDTO.class)))).thenReturn(funcionario);

        mockMvc.perform(post("/api/funcionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(funcionario)));
    }

    @Test
    void criarFuncionario_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Dados inválidos")).when(funcionarioService).cadastrarFuncionario(any(NovoFuncionarioDTO.class));

        mockMvc.perform(post("/api/funcionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoFuncionarioDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Dados inválidos'}"));
    }

    @Test
    void criarFuncionario_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao criar funcionário")).when(funcionarioService).cadastrarFuncionario(any(NovoFuncionarioDTO.class));

        mockMvc.perform(post("/api/funcionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoFuncionarioDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao criar funcionário'}"));
    }

    @Test
    void obterFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("João Silva");

        when(funcionarioMapper.toEntity(funcionarioService.obterFuncionario(1))).thenReturn(funcionario);

        mockMvc.perform(get("/api/funcionario/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'nome':'João Silva'}"));
    }

    @Test
    void obterFuncionario_ThrowsNoSuchElementException() throws Exception {
        when(funcionarioService.obterFuncionario(1)).thenThrow(new NoSuchElementException("Funcionário não encontrado"));

        mockMvc.perform(get("/api/funcionario/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Funcionário não encontrado'}"));
    }

    @Test
    void obterFuncionario_ThrowsException() throws Exception {
        when(funcionarioService.obterFuncionario(1)).thenThrow(new RuntimeException("Erro ao obter funcionário"));

        mockMvc.perform(get("/api/funcionario/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao obter funcionário'}"));
    }

    @Test
    void editarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("João Silva");

        when(funcionarioMapper.toEntity(funcionarioService.alterarFuncionario(any(int.class), any(NovoFuncionarioDTO.class)))).thenReturn(funcionario);

        mockMvc.perform(put("/api/funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoFuncionarioDTO())))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'nome':'João Silva'}"));
    }

    @Test
    void editarFuncionario_ThrowsIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Dados inválidos")).when(funcionarioService).alterarFuncionario(any(Integer.class), any(NovoFuncionarioDTO.class));

        mockMvc.perform(put("/api/funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoFuncionarioDTO())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'codigo':'422','mensagem':'Dados inválidos'}"));
    }

    @Test
    void editarFuncionario_ThrowsNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException("Funcionário não encontrado")).when(funcionarioService).alterarFuncionario(any(Integer.class), any(NovoFuncionarioDTO.class));

        mockMvc.perform(put("/api/funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoFuncionarioDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Funcionário não encontrado'}"));
    }

    @Test
    void editarFuncionario_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao editar funcionário")).when(funcionarioService).alterarFuncionario(any(Integer.class), any(NovoFuncionarioDTO.class));

        mockMvc.perform(put("/api/funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NovoFuncionarioDTO())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao editar funcionário'}"));
    }

    @Test
    void removerFuncionario() throws Exception {
        mockMvc.perform(delete("/api/funcionario/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Funcionário removido"));
    }

    @Test
    void removerFuncionario_ThrowsNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException("Funcionário não encontrado")).when(funcionarioService).excluirFuncionario(any(Integer.class));

        mockMvc.perform(delete("/api/funcionario/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'codigo':'404','mensagem':'Funcionário não encontrado'}"));
    }

    @Test
    void removerFuncionario_ThrowsException() throws Exception {
        doThrow(new RuntimeException("Erro ao remover funcionário")).when(funcionarioService).excluirFuncionario(any(Integer.class));

        mockMvc.perform(delete("/api/funcionario/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao remover funcionário'}"));
    }
}
