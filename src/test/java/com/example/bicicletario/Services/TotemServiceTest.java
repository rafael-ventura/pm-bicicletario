package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.TotemService;
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
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
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
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemRepository.findAll()).thenReturn(List.of(totem));

        List<Totem> result = totemService.listarTotens();
        assertEquals(1, result.size());
    }

    @Test
    void cadastrarTotem() {
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        novoTotem.setLocalizacao("Localizacao");
        novoTotem.setDescricao("Descricao");

        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemMapper.toEntity(any(NovoTotemDTO.class))).thenReturn(totem);
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);

        Totem result = totemService.cadastrarTotem(novoTotem);
        assertNotNull(result);
    }

    @Test
    void cadastrarTotem_ThrowsInvalidDataException() {
        NovoTotemDTO novoTotem = new NovoTotemDTO();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            totemService.cadastrarTotem(novoTotem);
        });

        assertEquals(Constantes.DADOS_INVALIDOS, exception.getMessage());
    }

    @Test
    void editarTotem() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        Totem updatedTotem = new Totem();
        updatedTotem.setId(1L);
        updatedTotem.setLocalizacao("Nova Localizacao");
        updatedTotem.setDescricao("Nova Descricao");

        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.of(totem));
        when(totemRepository.save(any(Totem.class))).thenReturn(updatedTotem);

        NovoTotemDTO totemDTO = new NovoTotemDTO();
        totemDTO.setLocalizacao("Nova Localizacao");
        totemDTO.setDescricao("Nova Descricao");

        Totem result = totemService.editarTotem(1L, totemDTO);

        assertNotNull(result);
        assertEquals("Nova Localizacao", result.getLocalizacao());
        assertEquals("Nova Descricao", result.getDescricao());
    }

    @Test
    void editarTotem_ThrowsResourceNotFoundException() {
        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NovoTotemDTO totemDTO = new NovoTotemDTO();
        totemDTO.setLocalizacao("Nova Localizacao");
        totemDTO.setDescricao("Nova Descricao");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.editarTotem(1L, totemDTO);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void editarTotem_ThrowsInvalidDataException() {
        Totem totem = new Totem();
        totem.setId(1L);

        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.of(totem));

        NovoTotemDTO totemDTO = new NovoTotemDTO();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            totemService.editarTotem(1L, totemDTO);
        });

        assertEquals(Constantes.DADOS_INVALIDOS, exception.getMessage());
    }

    @Test
    void removerTotem() {
        when(totemRepository.existsById(any(Long.class))).thenReturn(true);

        totemService.removerTotem(1L);

        verify(totemRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void removerTotem_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Long.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.removerTotem(1L);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void listarTrancas() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(totemRepository.existsById(any(Long.class))).thenReturn(true);
        when(trancaRepository.findByTotemId(any(Long.class))).thenReturn(List.of(tranca));

        List<Tranca> result = totemService.listarTrancas(1L);
        assertEquals(1, result.size());
    }

    @Test
    void listarTrancas_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Long.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.listarTrancas(1L);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        when(totemRepository.existsById(any(Long.class))).thenReturn(true);
        when(bicicletaRepository.findByTotemId(any(Long.class))).thenReturn(List.of(bicicleta));

        List<Bicicleta> result = totemService.listarBicicletas(1L);
        assertEquals(1, result.size());
    }

    @Test
    void listarBicicletas_ThrowsResourceNotFoundException() {
        when(totemRepository.existsById(any(Long.class))).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            totemService.listarBicicletas(1L);
        });

        assertEquals(Constantes.TOTEM_NAO_ENCONTRADO, exception.getMessage());
    }
}
