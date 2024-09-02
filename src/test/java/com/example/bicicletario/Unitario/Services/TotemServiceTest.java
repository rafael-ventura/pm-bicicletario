package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.services.TotemService;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.domain.mapper.TotemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private BicicletaRepository bicicletaRepository;

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

    /*@Test
    void excluirTotem() {
        Totem totem = new Totem();
        totem.setId(1);

        when(totemRepository.findById(1)).thenReturn(Optional.of(totem));
        when(totemRepository.existsById(1)).thenReturn(true);

        totemService.excluirTotem(1);

        verify(totemRepository, times(1)).deleteById(1);
    }*/


    @Test
    void excluirTotem_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Integer.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.excluirTotem(1);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }

    /*@Test
    void listarTrancas() {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        when(totemRepository.existsById(any(Integer.class))).thenReturn(true);
        when(trancaRepository.findByTotemLocalizacao(any(String.class))).thenReturn(List.of(tranca));

        List<Tranca> result = totemService.listarTrancasPorTotem(1);
        assertEquals(1, result.size());
    }*/

    @Test
    void listarTrancas_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Integer.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.listarTrancasPorTotem(1);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }

    /*@Test
    void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        when(totemRepository.existsById(any(Integer.class))).thenReturn(true);
        when(bicicletaRepository.findByTotemId(any(Integer.class))).thenReturn(List.of(bicicleta));

        List<Bicicleta> result = totemService.listarBicicletasPorTotem(1);
        assertEquals(1, result.size());
    }*/

    @Test
    void listarBicicletas_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Integer.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.listarBicicletasPorTotem(1);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }
}
