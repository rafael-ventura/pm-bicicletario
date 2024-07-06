package com.example.bicicletario;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BicicletaServiceTest {

    @InjectMocks
    private BicicletaService bicicletaService;

    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private TrancaRepository trancaRepository;

    @Mock
    private BicicletaMapper bicicletaMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");
        bicicleta.setAno("2021");
        bicicleta.setNumero(1);
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findAll()).thenReturn(List.of(bicicleta));

        List<Bicicleta> bicicletas = bicicletaService.listarBicicletas();
        assertEquals(1, bicicletas.size());
        assertEquals("marca", bicicletas.get(0).getMarca());
    }

    @Test
    public void criarBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        BicicletaDTO bicicletaDTO = new BicicletaDTO();

        when(bicicletaMapper.toBicicleta(any())).thenReturn(bicicleta);
        when(bicicletaRepository.save(any())).thenReturn(bicicleta);
        when(bicicletaMapper.toDto(any())).thenReturn(bicicletaDTO);

        BicicletaDTO bicicletaCriada = bicicletaService.criarBicicleta(bicicletaDTO);
        assertEquals(bicicletaDTO, bicicletaCriada);
    }

    @Test
    public void integrarNaRede() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        bicicletaService.integrarNaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    public void integrarNaRedeBicicletaInvalida() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals("Número da bicicleta inválido", exception.getMessage());
    }

    @Test
    public void integrarNaRedeTrancaOcupada() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.NOVA);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals("Tranca não encontrada", exception.getMessage());
    }

    @Test
    public void integrarNaRedeTrancaNaoEncontrada() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.NOVA);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals("Tranca não encontrada", exception.getMessage());
    }
}
