package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.exception.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import com.example.bicicletario.bicicletario.web.FuncionarioController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    @InjectMocks
    private FuncionarioController funcionarioController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void listarFuncionarios() throws Exception {
        NovoFuncionarioDTO funcionarioDTO = new NovoFuncionarioDTO();
        funcionarioDTO.setNome("Joao Silva");

        when(funcionarioService.listarFuncionarios()).thenReturn(List.of(funcionarioDTO));

        mockMvc.perform(get("/api/funcionario"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"nome\":\"Joao Silva\"}]"));
    }

    @Test
    void criarFuncionario() throws Exception {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("Joao Silva");
        dto.setSenha("senha123");
        dto.setConfirmacaoSenha("senha123");
        dto.setIdade(30);
        dto.setFuncao("Administrador");
        dto.setEmail("joao@gmail.com");
        dto.setCpf("12345678900");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("Joao Silva");

        when(funcionarioService.cadastrarFuncionario(any(NovoFuncionarioDTO.class))).thenReturn(funcionario);
        when(funcionarioMapper.toDto(any(Funcionario.class))).thenReturn(dto);

        mockMvc.perform(post("/api/funcionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"nome\":\"Joao Silva\"}"));
    }

    @Test
    void obterFuncionario() throws Exception {
        NovoFuncionarioDTO funcionarioDTO = new NovoFuncionarioDTO();
        funcionarioDTO.setNome("Joao Silva");

        when(funcionarioService.obterFuncionario(anyInt())).thenReturn(funcionarioDTO);

        mockMvc.perform(get("/api/funcionario/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"nome\":\"Joao Silva\"}"));
    }

    @Test
    void obterFuncionario_ThrowsException() throws Exception {
        when(funcionarioService.obterFuncionario(anyInt())).thenThrow(new InvalidDataException("Erro ao obter funcionario"));

        mockMvc.perform(get("/api/funcionario/1"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Erro ao obter funcionario\"}"));
    }

    @Test
    void editarFuncionario() throws Exception {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("Joao Silva");

        when(funcionarioService.alterarFuncionario(anyInt(), any(NovoFuncionarioDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"nome\":\"Joao Silva\"}"));
    }

    @Test
    void editarFuncionario_ThrowsResourceNotFoundException() throws Exception {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();

        doThrow(new ResourceNotFoundException("Funcionario nao encontrado")).when(funcionarioService).alterarFuncionario(anyInt(), any(NovoFuncionarioDTO.class));

        mockMvc.perform(put("/api/funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Funcionario nao encontrado\"}"));
    }

    @Test
    void removerFuncionario() throws Exception {
        doNothing().when(funcionarioService).excluirFuncionario(anyInt());

        mockMvc.perform(delete("/api/funcionario/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Funcionário excluído com sucesso"));
    }

    @Test
    void removerFuncionario_ThrowsResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Funcionario nao encontrado")).when(funcionarioService).excluirFuncionario(anyInt());

        mockMvc.perform(delete("/api/funcionario/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Funcionario nao encontrado\"}"));
    }

}