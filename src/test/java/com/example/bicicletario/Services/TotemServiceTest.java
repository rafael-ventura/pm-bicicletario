package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
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

        List<Totem> result = totemService.listarTotens();
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

        Totem result = totemService.cadastrarTotem(novoTotem);
        assertNotNull(result);
    }

    @Test
    void editarTotem() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localizacao");
        totem.setDescricao("Descricao");

        Totem Totem = new Totem();
        Totem.setId(1L);
        Totem.setLocalizacao("Nova Localizacao");
        Totem.setDescricao("Nova Descricao");

        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.of(totem));
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);

    }

    @Test
    void removerTotem() {
        Totem totem = new Totem();
        totem.setId(1L);

        when(totemRepository.findById(any(Long.class))).thenReturn(Optional.of(totem));
        when(trancaRepository.existsByTotemId(any(Long.class))).thenReturn(false);

        totemService.removerTotem(1L);

        verify(totemRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void listarTrancas() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findByTotemId(any(Long.class))).thenReturn(List.of(tranca));
        when(trancaMapper.toDtoList(any())).thenReturn(List.of(new Tranca()));

        List<Tranca> result = totemService.listarTrancas(1L);
        assertEquals(1, result.size());
    }

    @Test
    void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        when(bicicletaRepository.findByTrancaId(any(Long.class))).thenReturn(List.of(bicicleta));
        when(bicicletaMapper.toDtoList(any())).thenReturn(List.of(new Bicicleta()));

        List<Bicicleta> result = totemService.listarBicicletas(1L);
        assertEquals(1, result.size());
    }
}
