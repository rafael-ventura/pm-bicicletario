package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TotemServiceTest {

    @InjectMocks
    private TotemService totemService;

    @Mock
    private TotemRepository totemRepository;

    @Mock
    private TrancaRepository trancaRepository;

    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private BicicletaMapper bicicletaMapper;

    @Mock
    private TotemMapper totemMapper;

    @Mock
    private TrancaMapper trancaMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listarTotens() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemRepository.findAll()).thenReturn(List.of(totem));
        when(totemMapper.toEntityList(any())).thenReturn(List.of(new TotemDTO()));

        List<TotemDTO> result = totemService.listarTotens();
        assertEquals(1, result.size());
    }

    @Test
    public void cadastrarTotem() {
        NovoTotemDTO novoTotem = new NovoTotemDTO();
        novoTotem.setLocalizacao("Localizacao");
        novoTotem.setDescricao("Descricao");

        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        when(totemMapper.toEntity(any(NovoTotemDTO.class))).thenReturn(totem);
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);
        when(totemMapper.toDto(any(Totem.class))).thenReturn(new TotemDTO());

        TotemDTO result = totemService.cadastrarTotem(novoTotem);
        assertNotNull(result);
    }

    @Test
    public void editarTotem() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        TotemDTO totemDTO = new TotemDTO();
        totemDTO.setId(1L);
        totemDTO.setLocalizacao("Nova Localizacao");
        totemDTO.setDescricao("Nova Descricao");

        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.of(totem));
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);
        when(totemMapper.toDto(any(Totem.class))).thenReturn(totemDTO);

        TotemDTO result = totemService.editarTotem(1L, totemDTO);
        assertNotNull(result);
    }

    @Test
    public void removerTotem() {
        Totem totem = new Totem();
        totem.setId(1L);

        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.of(totem));
        when(trancaRepository.existsByTotemId(any(Long.class))).thenReturn(false);

        totemService.removerTotem(1L);

        verify(totemRepository, times(1)).delete(totem);
    }

    @Test
    public void listarTrancas() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findByTotemId(any(Long.class))).thenReturn(List.of(tranca));
        when(trancaMapper.toDtoList(any())).thenReturn(List.of(new TrancaDTO()));

        List<TrancaDTO> result = totemService.listarTrancas(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        when(bicicletaRepository.findByTotemId(any(Long.class))).thenReturn(List.of(bicicleta));
        when(bicicletaMapper.toDtoList(any())).thenReturn(List.of(new BicicletaDTO()));

        List<BicicletaDTO> result = totemService.listarBicicletas(1L);
        assertEquals(1, result.size());
    }
}
