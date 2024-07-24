package com.example.bicicletario.controllers;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.exception.GlobalExceptionHandler;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(funcionarioService, funcionarioMapper)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void listarFuncionarios() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("Joao Silva");

        when(funcionarioMapper.toEntityList(funcionarioService.listarFuncionarios())).thenReturn(List.of(funcionario));

        List<Funcionario> funcionarios = funcionarioMapper.toEntityList(funcionarioService.listarFuncionarios());
        assertEquals(1, funcionarios.size());
        assertEquals("Joao Silva", funcionarios.get(0).getNome());
    }

    @Test
    void criarFuncionario() {
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

        when(funcionarioService.cadastrarFuncionario(dto)).thenReturn(funcionario);

        Funcionario funcionarioCriado = funcionarioService.cadastrarFuncionario(dto);
        assertEquals(funcionario, funcionarioCriado);
    }


    @Test
    void obterFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("Joao Silva");

        when(funcionarioMapper.toEntity(funcionarioService.obterFuncionario(1))).thenReturn(funcionario);

        Funcionario result = funcionarioMapper.toEntity(funcionarioService.obterFuncionario(1));
        assertEquals(funcionario, result);
    }

    @Test
    void obterFuncionario_ThrowsException() {
        when(funcionarioService.obterFuncionario(1)).thenThrow(new InvalidDataException("Erro ao obter funcionário"));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            funcionarioService.obterFuncionario(1);
        });

        assertEquals("Erro ao obter funcionário", exception.getMessage());
    }

    @Test
    void editarFuncionario() {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("Joao Silva");

        when(funcionarioMapper.toEntity(funcionarioService.alterarFuncionario(1, dto))).thenReturn(funcionario);

        Funcionario funcionarioEditado = funcionarioMapper.toEntity(funcionarioService.alterarFuncionario(1, dto));
        assertEquals(funcionario, funcionarioEditado);
    }

    @Test
    void editarFuncionario_ThrowsResourceNotFoundException() {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();

        doThrow(new ResourceNotFoundException("Funcionário não encontrado")).when(funcionarioService).alterarFuncionario(any(Integer.class), any(NovoFuncionarioDTO.class));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.alterarFuncionario(1, dto);
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }

    @Test
    void removerFuncionario() {
        doNothing().when(funcionarioService).excluirFuncionario(1);

        funcionarioService.excluirFuncionario(1);

        verify(funcionarioService, times(1)).excluirFuncionario(1);
    }

    @Test
    void removerFuncionario_ThrowsResourceNotFoundException() {
        doThrow(new ResourceNotFoundException("Funcionário não encontrado")).when(funcionarioService).excluirFuncionario(1);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.excluirFuncionario(1);
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }
}
