package com.example.bicicletario.services.unitarios;

import com.example.bicicletario.bicicletario.application.DevolucaoService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.DevolucaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    void setUp() {
        devolucaoRepository = mock(DevolucaoRepository.class);
        aluguelRepository = mock(AluguelRepository.class);
        bicicletaService = mock(BicicletaService.class);
        trancaService = mock(TrancaService.class);
        administradoraCCService = mock(AdministradoraCCService.class);
        emailService = mock(EmailService.class);

        devolucaoService = new DevolucaoService(devolucaoRepository, aluguelRepository, bicicletaService, trancaService, administradoraCCService, emailService);
    }

    @Test
    void realizarDevolucao_Success() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        NovoTrancaDTO tranca = new NovoTrancaDTO();
        tranca.setStatus(StatusTranca.LIVRE);

        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        aluguel.setHoraInicio(LocalDateTime.now().minusHours(2).toString());
        aluguel.setCiclista(1);

        when(bicicletaService.getBicicleta()).thenReturn(Optional.of(bicicleta));
        when(trancaService.obterTranca()).thenReturn(Optional.of(tranca));
        when(aluguelRepository.findByBicicletaAndHoraFimIsNull(1)).thenReturn(Optional.of(aluguel));
        when(administradoraCCService.processarPagamento()).thenReturn(true);

        // Act
        Devolucao devolucao = devolucaoService.realizarDevolucao(devolucaoDTO);

        // Assert
        assertNotNull(devolucao);
        assertEquals(1, devolucao.getIdBicicleta());
        assertEquals(2, devolucao.getIdTranca());
        assertEquals("SUCESSO", devolucao.getStatusPagamento());

        verify(devolucaoRepository).save(any(Devolucao.class));
        verify(emailService).enviarEmailDevolucao(anyInt(), any(Devolucao.class));
    }

    @Test
    void realizarDevolucao_BicicletaNaoEmUso() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(bicicletaService.getBicicleta()).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> devolucaoService.realizarDevolucao(devolucaoDTO));

        assertEquals("Bicicleta não está em uso.", exception.getMessage());
    }

    @Test
    void realizarDevolucao_TrancaNaoDisponivel() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        NovoTrancaDTO tranca = new NovoTrancaDTO();
        tranca.setStatus(StatusTranca.OCUPADA);

        when(bicicletaService.getBicicleta()).thenReturn(Optional.of(bicicleta));
        when(trancaService.obterTranca()).thenReturn(Optional.of(tranca));

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> devolucaoService.realizarDevolucao(devolucaoDTO));

        assertEquals("Tranca não está disponível para devolução.", exception.getMessage());
    }

    @Test
    void realizarDevolucao_AluguelNaoEncontrado() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        NovoTrancaDTO tranca = new NovoTrancaDTO();
        tranca.setStatus(StatusTranca.LIVRE);

        when(bicicletaService.getBicicleta()).thenReturn(Optional.of(bicicleta));
        when(trancaService.obterTranca()).thenReturn(Optional.of(tranca));
        when(aluguelRepository.findByBicicletaAndHoraFimIsNull(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> devolucaoService.realizarDevolucao(devolucaoDTO));

        assertEquals("Aluguel ativo não encontrado para esta bicicleta.", exception.getMessage());
    }

    @Test
    void realizarDevolucao_ComPagamentoExtra() {
        // Arrange
        NovoDevolucaoDTO devolucaoDTO = new NovoDevolucaoDTO();
        devolucaoDTO.setIdBicicleta(1);
        devolucaoDTO.setIdTranca(2);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        NovoTrancaDTO tranca = new NovoTrancaDTO();
        tranca.setStatus(StatusTranca.LIVRE);

        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        aluguel.setHoraInicio(LocalDateTime.now().minusHours(3).toString()); // Mais de 2 horas de uso
        aluguel.setCiclista(1);

        when(bicicletaService.getBicicleta()).thenReturn(Optional.of(bicicleta));
        when(trancaService.obterTranca()).thenReturn(Optional.of(tranca));
        when(aluguelRepository.findByBicicletaAndHoraFimIsNull(1)).thenReturn(Optional.of(aluguel));
        when(administradoraCCService.processarPagamento()).thenReturn(true);

        // Act
        Devolucao devolucao = devolucaoService.realizarDevolucao(devolucaoDTO);

        // Assert
        assertNotNull(devolucao);
        assertTrue(devolucao.getValorExtra() > 0);
        assertEquals("SUCESSO", devolucao.getStatusPagamento());

        verify(administradoraCCService).processarPagamento();
    }
}
