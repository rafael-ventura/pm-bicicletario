package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.TrancaDTO;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    @Mock
    private TrancaRepository trancaRepository;

    @Mock
    private TrancaMapper trancaMapper;

    @InjectMocks
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void integrarNaRedeTrancaNaoDisponivel() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trancaService.integrarNaRede(dto);
        });

        assertEquals("Tranca não está disponível", exception.getMessage());
    }

    @Test
    void integrarNaRedeTrancaDisponivel() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        trancaService.integrarNaRede(dto);

        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void retirarDaRedeTrancaInvalida() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trancaService.retirarDaRede(dto);
        });

        assertEquals("Número da tranca inválido", exception.getMessage());
    }

    @Test
    void retirarDaRedeTrancaComBicicleta() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trancaService.retirarDaRede(dto);
        });

        assertEquals("Tranca está com bicicleta presa", exception.getMessage());
    }

    @Test
    void retirarDaRedeTrancaValida() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);
        dto.setStatusAcaoReparador(StatusAcaoReparador.EM_REPARO);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        trancaService.retirarDaRede(dto);

        assertEquals(StatusTranca.EM_REPARO, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void retirarDaRedeStatusAcaoInvalido() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);
        dto.setStatusAcaoReparador(null);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trancaService.retirarDaRede(dto);
        });

        assertEquals("Status de ação do reparador inválido", exception.getMessage());
    }


    @Test
    void listarTrancas() {
        trancaService.listarTrancas();
        verify(trancaRepository, times(1)).findAll();
    }

    @Test
    void cadastrarTranca() {
        TrancaDTO trancaDTO = new TrancaDTO();
        Tranca tranca = new Tranca();

        when(trancaMapper.toTranca(trancaDTO)).thenReturn(tranca);
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.cadastrarTranca(trancaDTO);

        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void obterTrancaInvalida() {
        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trancaService.obterTranca(1L);
        });

        assertEquals("Tranca não encontrada", exception.getMessage());
    }

    @Test
    void obterTrancaValida() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        Tranca result = trancaService.obterTranca(1L);

        assertEquals(tranca, result);
    }

    @Test
    void editarTranca() {
        TrancaDTO trancaDTO = new TrancaDTO();
        trancaDTO.setStatus(StatusTranca.LIVRE.toString());

        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.editarTranca(1L, trancaDTO);

        assertEquals(StatusTranca.LIVRE, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void removerTranca() {
        trancaService.removerTranca(1L);
        verify(trancaRepository, times(1)).deleteById(1L);
    }

    @Test
    void obterBicicletaNaTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        Tranca result = trancaService.obterBicicletaNaTranca(1L);

        assertEquals(tranca, result);
    }

    @Test
    void trancarTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.trancarTranca(1L, 1L);

        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void destrancarTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.destrancarTranca(1L, 1L);

        assertEquals(StatusTranca.LIVRE, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void alterarStatusTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.alterarStatusTranca(1L, "OCUPADA");

        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }
}
