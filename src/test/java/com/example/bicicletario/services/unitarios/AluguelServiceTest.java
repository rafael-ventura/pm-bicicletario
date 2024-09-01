package com.example.bicicletario.services.unitarios;

import com.example.bicicletario.bicicletario.application.AluguelService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private BicicletaService bicicletaService;

    @Mock
    private TrancaService trancaService;

    @Mock
    private AdministradoraCCService administradoraCCService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AluguelService aluguelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   /* @Test
    void aluguel_success() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        trancaDTO.setStatus(StatusTranca.OCUPADA);
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(1)).thenReturn(Optional.of(trancaDTO));
        when(bicicletaService.getBicicletaByTranca(1)).thenReturn(Optional.of(bicicleta));
        when(administradoraCCService.enviarCobranca(cobrancaDTO)).thenReturn(true);
        doNothing().when(administradoraCCService).registrarCobrancaPendente(anyInt());
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Aluguel aluguel = aluguelService.aluguel(idCiclista, idTranca);

        // Assert
        assertNotNull(aluguel);
        verify(bicicletaService).atualizarStatus(bicicleta, StatusBicicleta.EM_USO);
        verify(trancaService).atualizarStatusTranca(idTranca, "DESTRANCAR");
        verify(emailService).enviarEmailAluguel(idCiclista, aluguel, bicicleta, trancaDTO);
    }*/

    @Test
    void aluguel_trancaNaoEncontrada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenThrow(new ResourceNotFoundException("Tranca não encontrada."));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Tranca não encontrada.", exception.getMessage());
    }

    @Test
    void aluguel_trancaNaoOcupada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setId(1);
        trancaDTO.setStatus(StatusTranca.LIVRE);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Tranca não está ocupada.", exception.getMessage());
    }

    @Test
    void aluguelNaoEncontrada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus(StatusTranca.OCUPADA);
        trancaDTO.setBicicleta(1);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);
        when(bicicletaService.getBicicletaByTranca(idTranca)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Bicicleta não encontrada.", exception.getMessage());
    }

    @Test
    void aluguelNaoDisponivel() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus(StatusTranca.OCUPADA);
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(trancaDTO);
        when(bicicletaService.getBicicletaByTranca(idTranca)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }

    @Test
    void aluguelEmReparo() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus(StatusTranca.OCUPADA);
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);

        // Configurando o comportamento dos mocks
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(trancaDTO);
        when(bicicletaService.getBicicletaByTranca(idTranca)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));

        // Verificações
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }
}
