package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
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
    private BicicletaRepository bicicletaRepository;

    @Mock
    private TrancaMapper trancaMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private FuncionarioService funcionarioService;

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

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.integrarNaRede(dto);
        });

        assertEquals("Status da tranca inválido", exception.getMessage());
    }

    @Test
    void integrarNaRedeTrancaDisponivel() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.NOVA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));
        doNothing().when(emailService).enviarEmailParaReparador(anyLong());

        trancaService.integrarNaRede(dto);

        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void integrarNaRedeFuncionarioInvalido() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.EM_REPARO);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(false);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.integrarNaRede(dto);
        });

        assertEquals("Funcionário inválido", exception.getMessage());
    }

    @Test
    void integrarNaRedeErroEnvioEmail() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.NOVA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);
        doThrow(new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL)).when(emailService).enviarEmailParaReparador(anyLong());

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.ERROR_ENVIAR_EMAIL, exception.getMessage());
    }


    @Test
    void retirarDaRedeTrancaValida() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);
        dto.setStatusAcaoReparador(StatusAcaoReparador.EM_REPARO);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));
        doNothing().when(emailService).enviarEmailParaReparador(anyLong());

        trancaService.retirarDaRede(dto);

        assertEquals(StatusTranca.EM_REPARO, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void retirarDaRedeTrancaComBicicleta() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.retirarDaRede(dto);
        });

        assertEquals(Constantes.TRANCA_PRENCHIDA, exception.getMessage());
    }

    @Test
    void retirarDaRedeStatusAcaoInvalido() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1L);
        dto.setStatusAcaoReparador(null);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
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
        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
        Tranca tranca = new Tranca();

        when(trancaMapper.toEntity(trancaDTO)).thenReturn(tranca);
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.cadastrarTranca(trancaDTO);

        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void obterTrancaInvalida() {
        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
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
        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
        trancaDTO.setStatus(StatusTranca.LIVRE);

        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(StatusTranca.LIVRE);

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
        tranca.setStatus(StatusTranca.LIVRE); // Certifique-se de que a tranca está livre inicialmente

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        trancaService.trancarTranca(1L, 1L);

        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
        verify(bicicletaRepository, times(1)).save(bicicleta);
    }

    @Test
    void destrancarTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(StatusTranca.OCUPADA); // Certifique-se de que a tranca está ocupada inicialmente

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        trancaService.destrancarTranca(1L, 1L);

        assertEquals(StatusTranca.LIVRE, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
        verify(bicicletaRepository, times(1)).save(bicicleta);
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
