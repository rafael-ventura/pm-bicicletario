package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.DevolucaoService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.DevolucaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevolucaoServiceTest {

    private DevolucaoService devolucaoService;
    private DevolucaoRepository devolucaoRepository;
    private AluguelRepository aluguelRepository;
    private BicicletaService bicicletaService;
    private TrancaService trancaService;
    private AdministradoraCCService administradoraCCService;
    private EmailService emailService;
    private CiclistaService ciclistaService;

    @BeforeEach
    void setUp() {
        devolucaoRepository = mock(DevolucaoRepository.class);
        aluguelRepository = mock(AluguelRepository.class);
        bicicletaService = mock(BicicletaService.class);
        trancaService = mock(TrancaService.class);
        administradoraCCService = mock(AdministradoraCCService.class);
        emailService = mock(EmailService.class);
        ciclistaService = mock(CiclistaService.class);

        devolucaoService = new DevolucaoService(devolucaoRepository, aluguelRepository, bicicletaService, trancaService, administradoraCCService, emailService, ciclistaService);
    }

    @Test
    void realizarDevolucao_Success() {
        // Arrange
        NovoCobrancaDTO novoCobranca = new NovoCobrancaDTO();
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(1);

        Ciclista ciclista = new Ciclista();

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.LIVRE);

        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        aluguel.setHoraInicio(LocalDateTime.now().minusHours(2).toString());
        aluguel.setCiclista(1);

        when(bicicletaService.getBicicletaById(devolucaoDTO.getIdBicicleta())).thenReturn(bicicleta);
        when(trancaService.obterTranca(devolucaoDTO.getIdTranca())).thenReturn(tranca);
        when(aluguelRepository.findByBicicletaAndHoraFimIsNull(devolucaoDTO.getIdBicicleta())).thenReturn(Optional.of(aluguel));
        when(ciclistaService.obterCiclista(aluguel.getCiclista())).thenReturn(Optional.of(ciclista));
        when(administradoraCCService.enviarCobranca(any(NovoCobrancaDTO.class))).thenReturn(true);

        // Act
        Devolucao devolucao1 = devolucaoService.realizarDevolucao(devolucaoDTO);

        // Assert
        assertNotNull(devolucao1);
        assertEquals(1, devolucao1.getIdBicicleta());
        assertEquals(1, devolucao1.getIdTranca());
        assertEquals("SUCESSO", devolucao1.getStatusPagamento());

        verify(devolucaoRepository).save(any(Devolucao.class));

        // Capturando os argumentos passados para o emailService.enviarEmailDevolucao
        ArgumentCaptor<String> ciclistaIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Aluguel> aluguelCaptor = ArgumentCaptor.forClass(Aluguel.class);
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        ArgumentCaptor<Tranca> trancaCaptor = ArgumentCaptor.forClass(Tranca.class);
        ArgumentCaptor<Double> valorExtraCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<String> cartaoUsadoCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusPagamentoCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataHoraCobrancaCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService).enviarEmailDevolucao(
                ciclistaIdCaptor.capture(),
                aluguelCaptor.capture(),
                bicicletaCaptor.capture(),
                trancaCaptor.capture(),
                valorExtraCaptor.capture(),
                cartaoUsadoCaptor.capture(),
                statusPagamentoCaptor.capture(),
                dataHoraCobrancaCaptor.capture()
        );

        // Verificando os valores capturados
        assertEquals(aluguel, aluguelCaptor.getValue());
        assertEquals(bicicleta, bicicletaCaptor.getValue());
        assertEquals(tranca, trancaCaptor.getValue());
        assertEquals(0.0, valorExtraCaptor.getValue());
        assertEquals("Dados do cartão do ciclista", cartaoUsadoCaptor.getValue());
        assertEquals("SUCESSO", statusPagamentoCaptor.getValue());
        assertNull(dataHoraCobrancaCaptor.getValue());
    }


    @Test
    void realizarDevolucao_BicicletaNaoEmUso() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaService.getBicicletaById(devolucaoDTO.getIdBicicleta())).thenReturn(bicicleta);

        // Mockando a tranca para evitar NullPointerException, mas não retornando nada específico
        Tranca tranca = new Tranca();
        tranca.setId(1);
        when(trancaService.obterTranca(devolucaoDTO.getIdTranca())).thenReturn(tranca);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> devolucaoService.realizarDevolucao(devolucaoDTO));

        assertEquals("Aluguel ativo não encontrado para esta bicicleta.", exception.getMessage());
    }


    @Test
    void realizarDevolucao_TrancaNaoDisponivel() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdTranca(1);
        devolucaoDTO.setIdBicicleta(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.OCUPADA);

        when(bicicletaService.getBicicletaById(bicicleta.getId())).thenReturn(bicicleta);
        when(trancaService.obterTranca(tranca.getId())).thenReturn(tranca);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> devolucaoService.realizarDevolucao(devolucaoDTO));

        assertEquals("Aluguel ativo não encontrado para esta bicicleta.", exception.getMessage());
    }

    @Test
    void realizarDevolucao_AluguelNaoEncontrado() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.LIVRE);

        when(bicicletaService.getBicicletaById(devolucaoDTO.getIdBicicleta())).thenReturn(bicicleta);
        when(trancaService.obterTranca(devolucaoDTO.getIdTranca())).thenReturn(tranca);
        when(aluguelRepository.findByBicicletaAndHoraFimIsNull(devolucaoDTO.getIdBicicleta())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> devolucaoService.realizarDevolucao(devolucaoDTO));

        assertEquals("Aluguel ativo não encontrado para esta bicicleta.", exception.getMessage());
    }

    @Test
    void realizarDevolucao_ComPagamentoExtra() {
        // Arrange
        NovoCobrancaDTO novoCobranca = new NovoCobrancaDTO();
        int idCiclista = 1;

        Devolucao devolucao = new Devolucao();
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdTranca(1);
        devolucaoDTO.setIdBicicleta(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        Tranca tranca = new Tranca();
        tranca.setId(1);
        tranca.setStatus(StatusTranca.LIVRE);

        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        aluguel.setHoraInicio(LocalDateTime.now().minusHours(3).toString()); // Mais de 2 horas de uso
        aluguel.setCiclista(idCiclista);

        when(bicicletaService.getBicicletaById(devolucaoDTO.getIdBicicleta())).thenReturn(bicicleta);
        when(trancaService.obterTranca(devolucaoDTO.getIdTranca())).thenReturn(tranca);
        when(aluguelRepository.findByBicicletaAndHoraFimIsNull(devolucaoDTO.getIdBicicleta())).thenReturn(Optional.of(aluguel));
        when(administradoraCCService.enviarCobranca(any(NovoCobrancaDTO.class))).thenReturn(true);

        // Act
        Devolucao devolucaoResult = devolucaoService.realizarDevolucao(devolucaoDTO);

        // Assert
        assertNotNull(devolucaoResult);
        assertTrue(devolucaoResult.getValorExtra() > 0);
        assertEquals("SUCESSO", devolucaoResult.getStatusPagamento());

        verify(administradoraCCService).enviarCobranca(any(NovoCobrancaDTO.class));
    }
}
