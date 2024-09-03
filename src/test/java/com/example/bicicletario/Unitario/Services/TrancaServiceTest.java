package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.EmailService;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.application.services.TrancaService;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.mapper.TrancaMapper;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
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
        dto.setIdTranca(1);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.incluirTrancaNaRede(dto);
        });

        assertEquals("Tranca não disponível", exception.getMessage());
    }

    @Test
    void integrarNaRedeTrancaDisponivel() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.NOVA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        trancaService.incluirTrancaNaRede(dto);

        verify(trancaRepository, times(1)).save(tranca);
        verify(emailService, times(1)).enviarEmailParaTranca(eq(dto.getIdFuncionario()), eq(tranca), eq("Inclusão"));
    }

    @Test
    void integrarNaRedeFuncionarioInvalido() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.EM_REPARO);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(false);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.incluirTrancaNaRede(dto);
        });

        assertEquals(Constantes.FUNCIONARIO_INVALIDO, exception.getMessage());
    }

    @Test
    void integrarNaRedeErroEnvioEmail() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.NOVA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);
        doThrow(new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL))
                .when(emailService).enviarEmailParaTranca(eq(dto.getIdFuncionario()), eq(tranca), eq("Inclusão"));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.incluirTrancaNaRede(dto);
        });

        assertEquals(Constantes.ERROR_ENVIAR_EMAIL, exception.getMessage());
    }

    @Test
    void retirarDaRedeTrancaValida() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1);
        dto.setStatusAcaoReparador(StatusAcaoReparador.EM_REPARO);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.EM_REPARO);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        trancaService.retirarTrancaDaRede(dto);

        assertEquals(StatusTranca.EM_REPARO, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
        verify(emailService, times(1)).enviarEmailParaTranca(eq(dto.getIdFuncionario()), eq(tranca), eq("Retirada"));
    }

    @Test
    void retirarDaRedeTrancaComBicicleta() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.retirarTrancaDaRede(dto);
        });

        assertEquals(Constantes.TRANCA_PRENCHIDA, exception.getMessage());
    }

    @Test
    void retirarDaRedeStatusAcaoInvalido() {
        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
        dto.setIdTranca(1);
        dto.setStatusAcaoReparador(null);

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);

        when(trancaRepository.findById(dto.getIdTranca())).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.retirarTrancaDaRede(dto);
        });

        assertEquals("Status de ação do reparador inválido", exception.getMessage());
    }

    @Test
    void listarTrancas() {
        trancaService.listarTodasTrancas();
        verify(trancaRepository, times(1)).findAll();
    }

    @Test
    void cadastrarTranca() {
        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
        trancaDTO.setLocalizacao("Rua A, 123"); // Adicionar campos obrigatórios
        trancaDTO.setModelo("Modelo X");
        trancaDTO.setAnoDeFabricacao("2024");

        Tranca tranca = new Tranca();
        when(trancaMapper.toEntity(trancaDTO)).thenReturn(tranca);
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.cadastrarNovaTranca(trancaDTO);

        verify(trancaRepository, times(1)).save(tranca);
    }


    @Test
    void obterTrancaInvalida() {
        when(trancaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            trancaService.obterTrancaPorId(1);
        });

        assertEquals(Constantes.NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void obterTrancaValida() {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        Tranca result = trancaService.obterTrancaPorId(1);

        assertEquals(tranca, result);
    }

    @Test
    void editarTranca() {
        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
        trancaDTO.setLocalizacao("Rua A, 123");
        trancaDTO.setModelo("Modelo X");
        trancaDTO.setAnoDeFabricacao("2024");
        trancaDTO.setStatus(StatusTranca.LIVRE);

        Tranca tranca = new Tranca();
        tranca.setId(1);

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.atualizarTranca(1, trancaDTO);

        assertEquals(StatusTranca.LIVRE, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }


    @Test
    void removerTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.LIVRE); // Suponha que a tranca está em um estado que permite remoção

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        trancaService.excluirTranca(1);

        verify(trancaRepository, times(1)).deleteById(1);
    }


    @Test
    void obterBicicletaNaTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        tranca.setBicicleta(bicicleta); // Certifique-se de que a bicicleta está associada

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        Bicicleta result = trancaService.obterBicicletaNaTranca(1);

        assertEquals(bicicleta, result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getId());
    }


    @Test
    void trancarTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.LIVRE);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        trancaService.trancarTranca(1, 1);

        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void destrancarTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.OCUPADA);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        tranca.setBicicleta(bicicleta);

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        trancaService.destrancarTranca(1, 1);

        assertEquals(StatusTranca.LIVRE, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void alterarStatusTranca() {
        Tranca tranca = new Tranca();
        tranca.setId(1);

        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.alterarStatusTranca(1, "TRANCAR");

        assertEquals(StatusTranca.OCUPADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void cadastrarTrancaComDadosInvalidos() {
        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
        trancaDTO.setModelo(null); // Dados inválidos

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            trancaService.cadastrarNovaTranca(trancaDTO);
        });

        assertEquals(Constantes.DADOS_INVALIDOS, exception.getMessage());
    }


}
