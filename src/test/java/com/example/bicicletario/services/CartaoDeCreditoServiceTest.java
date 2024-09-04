package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.mapper.CartaoDeCreditoMapper;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.CartaoDeCreditoRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartaoDeCreditoServiceTest {

    @Mock
    private CartaoDeCreditoRepository cartaoDeCreditoRepository;

    @Mock
    private CiclistaRepository ciclistaRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private CartaoDeCreditoMapper cartaoDeCreditoMapper;

    @Mock
    private AdministradoraCCService administradoraCCService;

    @InjectMocks
    private CartaoDeCreditoService cartaoDeCreditoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obterCartaoDeCredito_Success() {
        // Arrange
        CartaoDeCredito cartao = new CartaoDeCredito();
        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.of(cartao));

        // Act
        CartaoDeCredito result = cartaoDeCreditoService.obterCartaoDeCredito(1);

        // Assert
        assertNotNull(result);
        assertEquals(cartao, result);
    }

    @Test
    void obterCartaoDeCredito_NotFound() {
        // Arrange
        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartaoDeCreditoService.obterCartaoDeCredito(1);
        });

        assertEquals("Cartão de crédito não encontrado.", exception.getMessage());
    }

    @Test
    void alterarCartaoDeCredito_Success() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDTO.setNomeTitular("Nome");
        novoCartaoDTO.setNumero("1234567890123456");
        novoCartaoDTO.setValidade("2025-12-31");
        novoCartaoDTO.setCvv("123");

        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setEmail("test@example.com");

        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.of(cartaoDeCredito));
        doNothing().when(administradoraCCService).validarCartao(any(NovoCartaoDeCreditoDTO.class));
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(emailService.enviarEmail(any(EmailDTO.class))).thenReturn(true);

        // Act
        cartaoDeCreditoService.alterarCartaoDeCredito(1, novoCartaoDTO);

        // Assert
        verify(cartaoDeCreditoRepository).save(cartaoDeCredito);
        verify(emailService).enviarEmail(any(EmailDTO.class));
    }

    @Test
    void alterarCartaoDeCredito_NotFound() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDTO = new NovoCartaoDeCreditoDTO();
        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartaoDeCreditoService.alterarCartaoDeCredito(1, novoCartaoDTO);
        });

        assertEquals("Cartão de crédito não encontrado.", exception.getMessage());
    }

    @Test
    void validarCartaoDeCredito_Success() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDTO.setNomeTitular("Nome");
        novoCartaoDTO.setNumero("1234567890123456");
        novoCartaoDTO.setValidade("2025-12-31");
        novoCartaoDTO.setCvv("123");

        doNothing().when(administradoraCCService).validarCartao(novoCartaoDTO);

        // Act & Assert
        assertDoesNotThrow(() -> cartaoDeCreditoService.validarCartaoDeCredito(novoCartaoDTO));
    }

    @Test
    void validarCartaoDeCredito_InvalidData() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDTO.setNomeTitular(""); // Nome vazio para forçar erro

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            cartaoDeCreditoService.validarCartaoDeCredito(novoCartaoDTO);
        });

        assertEquals("Nome do titular do cartão é obrigatório.", exception.getMessage());
    }

    @Test
    void enviarEmailAlteracaoDeDados_Success() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("teste@exemplo.com");
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(emailService.enviarEmail(any(EmailDTO.class))).thenReturn(true);

        // Act
        cartaoDeCreditoService.enviarEmailAlteracaoDeDados(1);

        // Assert
        verify(emailService).enviarEmail(any(EmailDTO.class));
    }

    @Test
    void enviarEmailAlteracaoDeDados_CiclistaNotFound() {
        // Arrange
        when(ciclistaRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartaoDeCreditoService.enviarEmailAlteracaoDeDados(1);
        });

        assertEquals("Ciclista não encontrado com o ID:", exception.getMessage());
    }

    @Test
    void obterCartaoDeCredito_NaoEncontrado() {
        // Arrange
        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cartaoDeCreditoService.obterCartaoDeCredito(1);
        });
    }

    @Test
    void save_Success() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDTO = new NovoCartaoDeCreditoDTO();
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        when(cartaoDeCreditoMapper.toEntity(novoCartaoDTO)).thenReturn(cartaoDeCredito);

        // Act
        cartaoDeCreditoService.save(novoCartaoDTO, 1);

        // Assert
        verify(cartaoDeCreditoRepository).save(cartaoDeCredito);
    }

    @Test
    void validarCartaoDeCredito_InvalidNumber() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDTO.setNomeTitular("Nome");
        novoCartaoDTO.setNumero(null);
        novoCartaoDTO.setValidade("invalid-date"); // Invalid date format
        novoCartaoDTO.setCvv("123");

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            cartaoDeCreditoService.validarCartaoDeCredito(novoCartaoDTO);
        });

        assertEquals("Número do cartão de crédito inválido.", exception.getMessage());
    }

    @Test
    void enviarEmailAlteracaoDeDados_EmailNotSent() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("teste@exemplo.com");
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(emailService.enviarEmail(any(EmailDTO.class))).thenReturn(false); // Simulate email failure

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartaoDeCreditoService.enviarEmailAlteracaoDeDados(1);
        });

        assertEquals("Falha ao enviar email.", exception.getMessage());
    }

}
