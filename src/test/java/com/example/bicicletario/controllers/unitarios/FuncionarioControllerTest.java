package com.example.bicicletario.controllers.unitarios;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import com.example.bicicletario.bicicletario.web.FuncionarioController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class FuncionarioControllerTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    @InjectMocks
    private FuncionarioController funcionarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarFuncionarios_Success() {
        // Arrange
        NovoFuncionarioDTO funcionario1 = new NovoFuncionarioDTO();
        NovoFuncionarioDTO funcionario2 = new NovoFuncionarioDTO();
        when(funcionarioService.listarFuncionarios()).thenReturn(List.of(funcionario1, funcionario2));

        // Act
        ResponseEntity<List<NovoFuncionarioDTO>> response = funcionarioController.listarFuncionarios();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void cadastrarFuncionario_Success() {
        // Arrange
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        NovoFuncionarioDTO savedFuncionarioDTO = new NovoFuncionarioDTO();
        when(funcionarioService.cadastrarFuncionario(novoFuncionarioDTO)).thenReturn(funcionarioMapper.toEntity(novoFuncionarioDTO));
        when(novoFuncionarioDTO).thenReturn(savedFuncionarioDTO);

        // Act
        ResponseEntity<NovoFuncionarioDTO> response = funcionarioController.cadastrarFuncionario(novoFuncionarioDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(funcionarioMapper).toEntity(novoFuncionarioDTO);
    }

    @Test
    void obterFuncionario_Success() {
        // Arrange
        Integer idFuncionario = 1;
        NovoFuncionarioDTO funcionarioDTO = new NovoFuncionarioDTO();
        when(funcionarioService.obterFuncionario(idFuncionario)).thenReturn(funcionarioDTO);

        // Act
        ResponseEntity<NovoFuncionarioDTO> response = funcionarioController.obterFuncionario(idFuncionario);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(funcionarioDTO, response.getBody());
    }

    @Test
    void alterarFuncionario_Success() {
        // Arrange
        Integer idFuncionario = 1;
        NovoFuncionarioDTO novoFuncionarioDTO = new NovoFuncionarioDTO();
        NovoFuncionarioDTO updatedFuncionarioDTO = new NovoFuncionarioDTO();
        when(funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO)).thenReturn(updatedFuncionarioDTO);

        // Act
        ResponseEntity<NovoFuncionarioDTO> response = funcionarioController.alterarFuncionario(idFuncionario, novoFuncionarioDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(updatedFuncionarioDTO, response.getBody());
    }

    @Test
    void excluirFuncionario_Success() {
        // Arrange
        Integer idFuncionario = 1;
        doNothing().when(funcionarioService).excluirFuncionario(idFuncionario);

        // Act
        ResponseEntity<String> response = funcionarioController.excluirFuncionario(idFuncionario);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Funcionário excluído com sucesso!", response.getBody());
        verify(funcionarioService).excluirFuncionario(idFuncionario);
    }

    @Test
    void listarFuncionarios_NoContent() {
        // Arrange
        when(funcionarioService.listarFuncionarios()).thenReturn(List.of());

        // Act
        ResponseEntity<List<NovoFuncionarioDTO>> response = funcionarioController.listarFuncionarios();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void obterFuncionario_NotFound() {
        // Arrange
        Integer idFuncionario = 1;
        when(funcionarioService.obterFuncionario(idFuncionario)).thenReturn(null);

        // Act
        ResponseEntity<NovoFuncionarioDTO> response = funcionarioController.obterFuncionario(idFuncionario);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
