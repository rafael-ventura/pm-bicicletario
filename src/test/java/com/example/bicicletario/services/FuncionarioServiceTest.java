package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.FuncionarioRepository;
import com.example.bicicletario.bicicletario.domain.mapper.FuncionarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarFuncionario_Success() {
        // Arrange
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        novoFuncionarioDTO.setNome("Nome");
        novoFuncionarioDTO.setCpf("12345678901");
        novoFuncionarioDTO.setEmail("email@example.com");
        novoFuncionarioDTO.setSenha("senha123");
        novoFuncionarioDTO.setConfirmacaoSenha("senha123");
        novoFuncionarioDTO.setIdade(30);
        novoFuncionarioDTO.setFuncao("Admin");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);

        when(funcionarioMapper.toEntity(novoFuncionarioDTO)).thenReturn(funcionario);
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> {
            Funcionario savedFuncionario = invocation.getArgument(0);
            savedFuncionario.setMatricula("MAT-1234");
            return savedFuncionario;
        });

        // Act
        Funcionario result = funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);

        // Assert
        assertNotNull(result);
        assertEquals("MAT-1234", result.getMatricula());

        // ArgumentCaptor para capturar e verificar as propriedades do Funcionario salvo
        ArgumentCaptor<Funcionario> funcionarioCaptor = ArgumentCaptor.forClass(Funcionario.class);
        verify(funcionarioRepository).save(funcionarioCaptor.capture());
        Funcionario capturedFuncionario = funcionarioCaptor.getValue();
        assertEquals("Nome", capturedFuncionario.getNome());
        assertEquals("12345678901", capturedFuncionario.getCpf());
    }

    @Test
    void cadastrarFuncionario_InvalidData() {
        // Arrange
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO(); // DTO vazio para forçar erro

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);
        });

        assertEquals("Campos obrigatórios não preenchidos.", exception.getMessage());
    }

    @Test
    void cadastrarFuncionario_CpfInvalido() {
        // Arrange
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        novoFuncionarioDTO.setNome("Nome");
        novoFuncionarioDTO.setCpf("1234567"); // CPF inválido
        novoFuncionarioDTO.setEmail("email@example.com");
        novoFuncionarioDTO.setSenha("senha123");
        novoFuncionarioDTO.setConfirmacaoSenha("senha123");
        novoFuncionarioDTO.setIdade(30);
        novoFuncionarioDTO.setFuncao("Admin");

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);
        });

        assertEquals("CPF inválido. O CPF deve conter 11 dígitos e apenas números.", exception.getMessage());
    }

    @Test
    void cadastrarFuncionario_SenhaDiferente() {
        // Arrange
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        novoFuncionarioDTO.setNome("Nome");
        novoFuncionarioDTO.setCpf("12345678901");
        novoFuncionarioDTO.setEmail("email@example.com");
        novoFuncionarioDTO.setSenha("senha123");
        novoFuncionarioDTO.setConfirmacaoSenha("senha1234"); // Senha diferente

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);
        });

        assertEquals("As senhas não coincidem.", exception.getMessage());
    }

    @Test
    void listarFuncionarios_Success() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        List<Funcionario> funcionarios = List.of(funcionario);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);
        when(funcionarioMapper.toDtoList(funcionarios)).thenReturn(List.of(new NovoFuncionarioDTO()));

        // Act
        List<NovoFuncionarioDTO> result = funcionarioService.listarFuncionarios();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(funcionarioRepository).findAll();
    }

    @Test
    void obterFuncionario_Success() {
        // Arrange
        int idFuncionario = 1;
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario);
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.of(funcionario));
        when(funcionarioMapper.toDto(funcionario)).thenReturn(novoFuncionarioDTO);

        // Act
        NovoFuncionarioDTO result = funcionarioService.obterFuncionario(idFuncionario);

        // Assert
        assertNotNull(result);
        verify(funcionarioRepository).findById(idFuncionario);
    }

    @Test
    void obterFuncionario_NotFound() {
        // Arrange
        int idFuncionario = 1;
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.obterFuncionario(idFuncionario);
        });

        assertEquals("Funcionário não encontrado com o ID: " + idFuncionario, exception.getMessage());
    }

    @Test
    void alterarFuncionario_Success() {
        // Arrange
        int idFuncionario = 1;
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        novoFuncionarioDTO.setNome("Novo Nome");
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario);

        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        // Act
        NovoFuncionarioDTO result = funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO);

        // Assert
        assertNotNull(result);
        verify(funcionarioRepository).save(funcionario);
    }

    @Test
    void alterarFuncionario_NotFound() {
        // Arrange
        int idFuncionario = 1;
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO);
        });

        assertEquals("Funcionário não encontrado com o ID: " + idFuncionario, exception.getMessage());
    }

    @Test
    void excluirFuncionario_Success() {
        // Arrange
        int idFuncionario = 1;
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario);
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.of(funcionario));

        // Act
        funcionarioService.excluirFuncionario(idFuncionario);

        // Assert
        verify(funcionarioRepository).delete(funcionario);
    }

    @Test
    void excluirFuncionario_NotFound() {
        // Arrange
        int idFuncionario = 1;
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.excluirFuncionario(idFuncionario);
        });

        assertEquals("Funcionário não encontrado com o ID: " + idFuncionario, exception.getMessage());
    }
}
