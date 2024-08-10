package com.example.bicicletario.services.unitarios;

import com.example.bicicletario.bicicletario.application.AluguelService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
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

    @Test
    void alugarBicicleta_success() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus("ocupada");
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(Optional.of(trancaDTO));
        when(bicicletaService.getBicicleta(1)).thenReturn(Optional.of(bicicleta));
        when(administradoraCCService.processarPagamento(idCiclista, 10.00)).thenReturn(true);
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Aluguel aluguel = aluguelService.alugarBicicleta(idCiclista, idTranca);

        // Assert
        assertNotNull(aluguel);
        verify(bicicletaService).atualizarStatus(bicicleta, StatusBicicleta.EM_USO);
        verify(trancaService).atualizarStatusTranca(idTranca, "livre");
        verify(emailService).enviarEmailAluguel(idCiclista, aluguel);
    }

    @Test
    void alugarBicicleta_ciclistaJaPossuiAluguelAtivo() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(true);

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> aluguelService.alugarBicicleta(idCiclista, idTranca));
        assertEquals("Ciclista já possui um aluguel ativo.", exception.getMessage());
    }

    @Test
    void alugarBicicleta_trancaNaoEncontrada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> aluguelService.alugarBicicleta(idCiclista, idTranca));
        assertEquals("Tranca não encontrada.", exception.getMessage());
    }

    @Test
    void alugarBicicleta_trancaNaoOcupada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus("livre");

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(Optional.of(trancaDTO));

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> aluguelService.alugarBicicleta(idCiclista, idTranca));
        assertEquals("Tranca não está ocupada.", exception.getMessage());
    }

    @Test
    void alugarBicicleta_bicicletaNaoEncontrada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus("ocupada");
        trancaDTO.setBicicleta(1);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(Optional.of(trancaDTO));
        when(bicicletaService.getBicicleta(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> aluguelService.alugarBicicleta(idCiclista, idTranca));
        assertEquals("Bicicleta não encontrada.", exception.getMessage());
    }

    @Test
    void alugarBicicleta_bicicletaNaoDisponivel() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus("ocupada");
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(Optional.of(trancaDTO));
        when(bicicletaService.getBicicleta(1)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.alugarBicicleta(idCiclista, idTranca));
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }

    @Test
    void alugarBicicleta_bicicletaEmReparo() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        trancaDTO.setStatus("ocupada");
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);

        // Configurando o comportamento dos mocks
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(Optional.of(trancaDTO));
        when(bicicletaService.getBicicleta(1)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.alugarBicicleta(idCiclista, idTranca));

        // Verificações
        assertEquals("Bicicleta não pode ser alugada.", exception.getMessage());
    }
}
