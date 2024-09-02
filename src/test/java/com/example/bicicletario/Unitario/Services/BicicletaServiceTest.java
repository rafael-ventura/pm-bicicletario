package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.BicicletaService;
import com.example.bicicletario.bicicletario.application.services.EmailService;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.mapper.BicicletaMapper;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
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
    void cadastrarBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        NovaBicicletaDTO bicicletaDTO = new NovaBicicletaDTO();

        when(bicicletaMapper.toEntity(any())).thenReturn(bicicleta);
        when(bicicletaRepository.save(any())).thenReturn(bicicleta);

        Bicicleta bicicletaCriada = bicicletaService.cadastrarBicicleta(bicicletaDTO);
        assertEquals(bicicleta, bicicletaCriada);
    }

    @Test
    void obterBicicletaPorId() {
        Bicicleta bicicleta = new Bicicleta();
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        Bicicleta result = bicicletaService.obterBicicletaPorId(1);
        assertEquals(bicicleta, result);
    }

    @Test
    void obterBicicletaPorIdInvalida() {
        when(bicicletaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.obterBicicletaPorId(1);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void removerBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.APOSENTADA);
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        bicicletaService.excluirBicicleta(1);
        verify(bicicletaRepository, times(1)).deleteById(1);
    }

    @Test
    void removerBicicletaInvalida() {
        when(bicicletaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.excluirBicicleta(1);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void removerBicicletaNaoAposentada() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            bicicletaService.excluirBicicleta(1);
        });

        assertEquals(Constantes.BICICLETA_NAO_APOSENTADA, exception.getMessage());
    }

    @Test
    void atualizarBicicleta() {

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

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> {
            Bicicleta savedBicicleta = invocation.getArgument(0);
            savedBicicleta.setMarca(bicicletaDTO.getMarca());
            savedBicicleta.setModelo(bicicletaDTO.getModelo());
            savedBicicleta.setAno(bicicletaDTO.getAno());
            savedBicicleta.setStatusBicicleta(bicicletaDTO.getStatus());
            return savedBicicleta;
        });

        Bicicleta result = bicicletaService.atualizarBicicleta(1, bicicletaDTO);

        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals("nova marca", result.getMarca());
        assertEquals("novo modelo", result.getModelo());
        assertEquals("2022", result.getAno());
        assertEquals(1, result.getNumero()); // O número não deve ser alterado
        assertEquals(StatusBicicleta.DISPONIVEL, result.getStatusBicicleta());
    }

    @Test
    void atualizarBicicletaInvalida() {
        NovaBicicletaDTO bicicletaDTO = new NovaBicicletaDTO();
        when(bicicletaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.atualizarBicicleta(1, bicicletaDTO);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void integrarBicicletaNaRede() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        bicicletaService.integrarBicicletaNaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void integrarBicicletaNaRedeBicicletaInvalida() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void integrarBicicletaNaRedeTrancaNaoEncontrada() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        when(trancaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });

        assertEquals(Constantes.ID_TRANCA_INVALIDA, exception.getMessage());
    }

    @Test
    void integrarBicicletaNaRedeFuncionarioInvalido() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(false);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });

        assertEquals(Constantes.FUNCIONARIO_IGUAL, exception.getMessage());
    }

    @Test
    void integrarBicicletaNaRedeStatusBicicletaInvalido() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });

        assertEquals(Constantes.STATUS_DA_BICICLETA_INVALIDO, exception.getMessage());
    }

    /*@Test
    void integrarBicicletaNaRedeErroEnvioEmail() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        doThrow(new BadRequestException(Constantes.ERROR_ENVIAR_EMAIL))
                .when(emailService).enviarEmailParaReparador(eq(dto.getIdFuncionario()), anyString(), anyString());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });

        assertEquals(Constantes.ERROR_ENVIAR_EMAIL, exception.getMessage());
    }
*/

    @Test
    void retirarBicicletaDaRede() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);
        dto.setStatusAcaoReparador(StatusAcaoReparador.EM_REPARO);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        bicicletaService.retirarBicicletaDaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void retirarBicicletaDaRedeBicicletaInvalida() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });

        assertEquals(Constantes.BICICLETA_NAO_ENCONTRADA, exception.getMessage());
    }

    @Test
    void retirarBicicletaDaRedeTrancaNaoOcupada() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });

        assertEquals(Constantes.TRANCA_NAO_OCUPADA, exception.getMessage());
    }

    @Test
    void retirarBicicletaDaRedeTrancaNaoEncontrada() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        when(trancaRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });

        assertEquals(Constantes.ID_TRANCA_INVALIDA, exception.getMessage());
    }

    @Test
    void alterarStatusBicicletaDisponibilizar() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1, "disponivel");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.DISPONIVEL, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaReparar() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1, "reparo solicitado");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.REPARO_SOLICITADO, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaInvalido() {
        Bicicleta bicicleta = new Bicicleta();
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.alterarStatusBicicleta(1, "invalido");
        });

        assertEquals("Ação inválida", exception.getMessage());
    }

 /*   @Test
    void integrarBicicletaNaRedeBicicletaEmReparo() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.LIVRE);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));
        when(funcionarioService.isFuncionarioValido(dto.getIdFuncionario())).thenReturn(true);

        bicicletaService.integrarBicicletaNaRede(dto);
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(trancaRepository, times(1)).save(tranca);
    }*/

    @Test
    void integrarBicicletaNaRedeTrancaOcupada() {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });

        assertEquals(Constantes.TRANCA_NAO_DISPONIVEL, exception.getMessage());
    }

    @Test
    void retirarBicicletaDaRedeSemStatusReparador() {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1);
        dto.setIdTranca(1);
        dto.setIdFuncionario(1);
        dto.setStatusAcaoReparador(null);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        when(trancaRepository.findById(1)).thenReturn(Optional.of(tranca));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });

        assertEquals(Constantes.ACAO_INVALIDA, exception.getMessage());
    }

    @Test
    void alterarStatusBicicletaNova() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1, "nova");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.NOVA, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaEmUso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1, "em uso");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.EM_USO, result.getStatusBicicleta());
    }

    @Test
    void alterarStatusBicicletaAposentada() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bicicleta result = bicicletaService.alterarStatusBicicleta(1, "aposentada");
        verify(bicicletaRepository, times(1)).save(bicicleta);
        assertEquals(StatusBicicleta.APOSENTADA, result.getStatusBicicleta());
    }
}
