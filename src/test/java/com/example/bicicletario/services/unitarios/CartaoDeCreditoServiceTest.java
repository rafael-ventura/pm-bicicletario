package com.example.bicicletario.services.unitarios;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.CartaoDeCreditoRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import com.example.bicicletario.bicicletario.mapper.CartaoDeCreditoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
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

        // Mockando a resposta do repositório de Cartão de Crédito
        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.of(cartaoDeCredito));

        // Mockando a resposta do serviço de validação do cartão
        when(administradoraCCService.validarCartao(any(NovoCartaoDeCreditoDTO.class), eq(true))).thenReturn(true);

        // Mockando a resposta do repositório de Ciclista
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

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
        novoCartaoDTO.setNomeTitular("Nome Válido");
        novoCartaoDTO.setNumero("1234567890123456");
        novoCartaoDTO.setValidade("2025-12-31");
        novoCartaoDTO.setCvv("123");

        // Mockando a resposta do repositório para simular que o cartão não foi encontrado
        when(cartaoDeCreditoRepository.findByCiclistaId(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartaoDeCreditoService.alterarCartaoDeCredito(1, novoCartaoDTO);
        });

        // Verifica se a mensagem de erro está correta
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

        when(administradoraCCService.validarCartao(novoCartaoDTO, true)).thenReturn(true);

        // Act
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
    void enviarEmailAlteracaoDeDados_Success() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("teste@exemplo.com");
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

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
}
