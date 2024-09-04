package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.AluguelService;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private BicicletaService bicicletaService;

    @Mock
    private TrancaService trancaService;

    @Mock
    private CiclistaService ciclistaService;

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
    void aluguel_success() {
        // Arrange
        String emailCiclista = "email@test.com";
        int idCiclista = 1;
        int idTranca = 1;

        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        tranca.setBicicleta(bicicleta);

        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        ciclista.setEmail(emailCiclista);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);
        when(administradoraCCService.enviarCobranca(any())).thenReturn(true);
        when(ciclistaService.obterCiclista(idCiclista)).thenReturn(Optional.of(ciclista)); // Corrigido aqui
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Aluguel aluguel = aluguelService.aluguel(idCiclista, idTranca);

        // Assert
        assertNotNull(aluguel);
        assertEquals(idCiclista, aluguel.getCiclista());
        verify(trancaService).destrancarTranca(idTranca, bicicleta.getId());
        verify(emailService).enviarEmailAluguel(emailCiclista, aluguel, bicicleta, tranca);
    }


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
        trancaDTO.setBicicleta(1);
        trancaDTO.setLocalizacao("Localização");
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
        when(trancaService.getBicicletaByTranca(idTranca)).thenThrow(new ResourceNotFoundException("Bicicleta não encontrada."));

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Tranca não está ocupada.", exception.getMessage());
    }

    @Test
    void aluguelNaoDisponivel() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        NovoTrancaDTO trancaDTO = new NovoTrancaDTO();
        tranca.setStatus(StatusTranca.OCUPADA);
        trancaDTO.setBicicleta(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatus(StatusBicicleta.EM_USO);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);
        when(trancaService.getBicicletaByTranca(idTranca)).thenReturn(bicicleta);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Tranca sem bicicleta presa.", exception.getMessage());
    }

    @Test
    void aluguelEmReparo() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setStatus(StatusBicicleta.EM_REPARO);
        tranca.setBicicleta(bicicleta);

        // Configurando o comportamento dos mocks
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);
        when(trancaService.getBicicletaByTranca(idTranca)).thenReturn(bicicleta);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));

        // Verificações
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }


    @Test
    void aluguel_bicicletaNaoEncontrada() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;

        // Cria uma instância de Tranca e simula que ela está ocupada
        Tranca tranca = new Tranca();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        tranca.setBicicleta(bicicleta); // Associando uma bicicleta à tranca
        tranca.setStatus(StatusTranca.OCUPADA);

        // Simula que não existe aluguel em andamento para o ciclista
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);

        // Simula a obtenção da tranca
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);

        // Simula que ao tentar obter a bicicleta associada à tranca ocorre uma exceção de "Bicicleta não encontrada"
        when(trancaService.getBicicletaByTranca(idTranca)).thenThrow(new BadRequestException("Bicicleta não encontrada."));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));

        // Verifica se a mensagem da exceção é a esperada
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }

    @Test
    void aluguel_bicicletaNaoDisponivel() {
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.EM_USO);
        tranca.setBicicleta(bicicleta);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }

    @Test
    void aluguel_bicicletaEmReparo() {
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.EM_REPARO);
        tranca.setBicicleta(bicicleta);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Bicicleta não está disponível.", exception.getMessage());
    }

    @Test
    void aluguel_pagamentoNaoAutorizado() {
        int idCiclista = 1;
        int idTranca = 1;
        Tranca tranca = new Tranca();
        tranca.setStatus(StatusTranca.OCUPADA);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        tranca.setBicicleta(bicicleta);

        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);
        when(trancaService.obterTranca(idTranca)).thenReturn(tranca);
        when(administradoraCCService.enviarCobranca(any())).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> aluguelService.aluguel(idCiclista, idTranca));
        assertEquals("Pagamento não autorizado.", exception.getMessage());
        verify(administradoraCCService).registrarCobrancaPendente(idCiclista);
    }
}
