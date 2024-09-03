package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.TotemService;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.mapper.TotemMapper;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TotemServiceTest {

    @InjectMocks
    private TotemService totemService;

    @Mock
    private TotemRepository totemRepository;

    @Mock
    private TrancaRepository trancaRepository;

    @Mock
    private TotemMapper totemMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTotens() {
        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemRepository.findAll()).thenReturn(List.of(totem));

        List<Totem> result = totemService.listarTodosTotens();
        assertEquals(1, result.size());
    }

    @Test
    void cadastrarNovoTotem() {
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        novoTotem.setLocalizacao("Localizacao");
        novoTotem.setDescricao("Descricao");

        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemMapper.toEntity(any(NovoTotemDTO.class))).thenReturn(totem);
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);

        Totem result = totemService.cadastrarNovoTotem(novoTotem);
        assertNotNull(result);
    }

    @Test
    void cadastrarNovoTotem_ThrowsInvalidDataException() {
        NovoTotemDTO novoTotem = new NovoTotemDTO();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            totemService.cadastrarNovoTotem(novoTotem);
        });

        assertEquals(Constantes.DADOS_INVALIDOS, exception.getMessage());
    }

    @Test
    void atualizarTotem() {
        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        Totem updatedTotem = new Totem();
        updatedTotem.setId(1);
        updatedTotem.setLocalizacao("Nova Localizacao");
        updatedTotem.setDescricao("Nova Descricao");

        when(totemRepository.findById(any(Integer.class))).thenReturn(Optional.of(totem));
        when(totemRepository.save(any(Totem.class))).thenReturn(updatedTotem);

        NovoTotemDTO totemDTO = new NovoTotemDTO();
        totemDTO.setLocalizacao("Nova Localizacao");
        totemDTO.setDescricao("Nova Descricao");

        Totem result = totemService.atualizarTotem(1, totemDTO);

        assertNotNull(result);
        assertEquals("Nova Localizacao", result.getLocalizacao());
        assertEquals("Nova Descricao", result.getDescricao());
    }

    @Test
    void atualizarTotem_ThrowsResourceNotFoundException() {
        when(totemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        NovoTotemDTO totemDTO = new NovoTotemDTO();
        totemDTO.setLocalizacao("Nova Localizacao");
        totemDTO.setDescricao("Nova Descricao");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.atualizarTotem(1, totemDTO);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void atualizarTotem_ThrowsInvalidDataException() {
        Totem totem = new Totem();
        totem.setId(1);

        when(totemRepository.findById(any(Integer.class))).thenReturn(Optional.of(totem));

        NovoTotemDTO totemDTO = new NovoTotemDTO();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            totemService.atualizarTotem(1, totemDTO);
        });

        assertEquals(Constantes.DADOS_INVALIDOS, exception.getMessage());
    }

    @Test
    void excluirTotem() {
        // Criando um Totem válido
        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");

        // Mocking the repository methods
        when(totemRepository.findById(1)).thenReturn(Optional.of(totem));
        when(totemRepository.existsById(1)).thenReturn(true);
        when(totemRepository.get(1)).thenReturn(totem);
        when(totemService.listarTrancasPorTotem(1)).thenReturn(Collections.emptyList());
        when(trancaRepository.findTrancaByLocalizacao(totem.getLocalizacao())).thenReturn(Collections.emptyList());

        // Mock the delete method
        doNothing().when(totemRepository).deleteById(1);

        // Executa o método de exclusão
        totemService.excluirTotem(1);

        // Verifica se o método delete foi chamado uma vez
        verify(totemRepository, times(1)).deleteById(1);
    }


    @Test
    void excluirTotem_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Integer.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.excluirTotem(1);
        });

        assertEquals(Constantes.NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void listarTrancas() {
        // Criando um Totem válido
        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");

        // Criando uma Tranca associada ao Totem
        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setLocalizacao("Localizacao");

        // Mocking the repository methods
        when(totemRepository.existsById(1)).thenReturn(true);
        when(totemRepository.get(1)).thenReturn(totem);
        when(trancaRepository.findTrancaByLocalizacao("Localizacao")).thenReturn(List.of(tranca));

        // Executa o método para listar trancas
        List<Tranca> result = totemService.listarTrancasPorTotem(1);

        // Verifica o resultado
        assertEquals(1, result.size());
        assertEquals(tranca.getLocalizacao(), result.get(0).getLocalizacao());
    }


    @Test
    void listarTrancas_ThrowsResourceNotFoundException() {
        // Arrange
        when(totemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.listarTrancasPorTotem(1);
        });

        assertEquals(Constantes.NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("Marca");
        bicicleta.setModelo("Modelo");
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setLocalizacao("Localizacao");
        tranca.setBicicleta(bicicleta);
        tranca.setModelo("Modelo");
        tranca.setNumero(1);
        tranca.setStatus(StatusTranca.OCUPADA);

        List<Tranca> trancas = List.of(tranca);
        List<Bicicleta> bicicletas = List.of(bicicleta);

        Totem totem = new Totem();
        totem.setId(1);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemRepository.existsById(1)).thenReturn(true);
        when(totemRepository.get(1)).thenReturn(totem);
        when(trancaRepository.findTrancaByLocalizacao("Localizacao")).thenReturn(trancas);

        List<Bicicleta> result = totemService.listarBicicletasPorTotem(1);

        assertEquals(1, result.size());
        assertEquals(bicicleta.getMarca(), result.get(0).getMarca());
        assertEquals(bicicleta.getModelo(), result.get(0).getModelo());
        assertEquals(bicicleta.getId(), result.get(0).getId());
    }

    @Test
    void listarBicicletas_ThrowsResourceNotFoundException() {
        // Arrange
        when(totemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.listarBicicletasPorTotem(1);
        });

        assertEquals(Constantes.NAO_ENCONTRADO, exception.getMessage());
    }
}
