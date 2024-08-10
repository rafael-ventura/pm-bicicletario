package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BicicletaServiceTest {

    private final ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @InjectMocks
    private BicicletaService bicicletaService;
    @Mock
    private FuncionarioService funcionarioService;
    @Mock
    private EmailService emailService;
    @Mock
    private BicicletaRepository bicicletaRepository;
    @Mock
    private TrancaRepository trancaRepository;
    @Mock
    private BicicletaMapper bicicletaMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(consoleContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void listarBicicletas() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");
        bicicleta.setAno("2021");
        bicicleta.setNumero(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findAll()).thenReturn(List.of(bicicleta));

        List<Bicicleta> bicicletas = bicicletaService.listarBicicletas();
        assertEquals(1, bicicletas.size());
        assertEquals("marca", bicicletas.get(0).getMarca());
    }

    @Test
    void criarBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        NovaBicicletaDTO bicicletaDTO = new NovaBicicletaDTO();

        when(bicicletaMapper.toEntity(any())).thenReturn(bicicleta);
        when(bicicletaRepository.save(any())).thenReturn(bicicleta);

        Bicicleta bicicletaCriada = bicicletaService.criarBicicleta(bicicletaDTO);
        assertEquals(bicicleta, bicicletaCriada);
    }

    @Test
    void obterBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Bicicleta result = bicicletaService.obterBicicleta(1L);
        assertEquals(bicicleta, result);
    }

    @Test
    void obterBicicletaInvalida() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.obterBicicleta(1L);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void removerBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.APOSENTADA);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        bicicletaService.removerBicicleta(1L);
        verify(bicicletaRepository, times(1)).deleteById(1L);
    }

    @Test
    void removerBicicletaInvalida() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.removerBicicleta(1L);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void removerBicicletaNaoAposentada() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            bicicletaService.removerBicicleta(1L);
        });

        assertEquals(Constantes.BICICLETA_NAO_APOSENTADA, exception.getMessage());
    }

    @Test
    void editarBicicleta() {

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("marca antiga");
        bicicleta.setModelo("modelo antigo");
        bicicleta.setAno("2020");
        bicicleta.setNumero(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        NovaBicicletaDTO bicicletaDTO = new NovaBicicletaDTO();
        bicicletaDTO.setMarca("nova marca");
        bicicletaDTO.setModelo("novo modelo");
        bicicletaDTO.setAno("2022");
        bicicletaDTO.setNumero(2);
        bicicletaDTO.setStatus(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> {
            Bicicleta savedBicicleta = invocation.getArgument(0);
            savedBicicleta.setMarca(bicicletaDTO.getMarca());
            savedBicicleta.setModelo(bicicletaDTO.getModelo());
            savedBicicleta.setAno(bicicletaDTO.getAno());
            savedBicicleta.setStatusBicicleta(bicicletaDTO.getStatus());
            return savedBicicleta;
        });

        Bicicleta result = bicicletaService.editarBicicleta(1L, bicicletaDTO);

        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals("nova marca", result.getMarca());
        assertEquals("novo modelo", result.getModelo());
        assertEquals("2022", result.getAno());
        assertEquals(1, result.getNumero()); // O número não deve ser alterado
        assertEquals(StatusBicicleta.DISPONIVEL, result.getStatusBicicleta());
    }

    @Test
    void editarBicicletaInvalida() {
        NovaBicicletaDTO bicicletaDTO = new NovaBicicletaDTO();
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.editarBicicleta(1L, bicicletaDTO);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void integrarNaRede() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        bicicletaService.integrarNaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void integrarNaRedeBicicletaInvalida() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void integrarNaRedeTrancaNaoEncontrada() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.TRANCA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void integrarNaRedeFuncionarioInvalido() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(2L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(false);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.FUNCIONARIO_INVALIDO, exception.getMessage());
    }

    @Test
    void integrarNaRedeStatusBicicletaInvalido() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.STATUS_DA_BICICLETA_INVALIDO, exception.getMessage());
    }

    @Test
    void integrarNaRedeErroEnvioEmail() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);
        doThrow(new RuntimeException()).when(emailService).enviarEmailParaReparador(anyLong());

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.ERROR_ENVIAR_EMAIL, exception.getMessage());
    }

    @Test
    void retirarDaRede() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);
        dto.setStatusAcaoReparador(StatusAcaoReparador.EM_REPARO);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        bicicletaService.retirarDaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void retirarDaRedeBicicletaInvalida() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.retirarDaRede(dto);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void retirarDaRedeTrancaNaoOcupada() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.retirarDaRede(dto);
        });

        assertEquals(Constantes.TRANCA_NAO_OCUPADA, exception.getMessage());
    }

    @Test
    void retirarDaRedeTrancaNaoEncontrada() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.retirarDaRede(dto);
        });

        assertEquals(Constantes.TRANCA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void alterarStatusBicicletaDisponibilizar() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1L, "disponivel");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.DISPONIVEL, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaReparar() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1L, "reparo solicitado");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.REPARO_SOLICITADO, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaInvalido() {
        Bicicleta bicicleta = new Bicicleta();
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.alterarStatusBicicleta(1L, "invalido");
        });

        assertEquals("Ação inválida", exception.getMessage());
    }

    @Test
    void integrarNaRedeBicicletaEmReparo() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        bicicletaService.integrarNaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void integrarNaRedeTrancaOcupada() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarNaRede(dto);
        });

        assertEquals(Constantes.TRANCA_NAO_DISPONIVEL, exception.getMessage());
    }

    @Test
    void retirarDaRedeSemStatusReparador() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);
        dto.setStatusAcaoReparador(null);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.retirarDaRede(dto);
        });

        assertEquals(Constantes.ACAO_INVALIDA, exception.getMessage());
    }

    @Test
    void alterarStatusBicicletaNova() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1L, "nova");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.NOVA, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaEmUso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1L, "em uso");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.EM_USO, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaAposentada() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1L, "aposentada");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.APOSENTADA, result.getStatusBicicleta());
    }
}
