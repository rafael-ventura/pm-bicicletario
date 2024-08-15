package com.example.bicicletario.services;

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
    void testObterCartaoDeCredito() {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        when(cartaoDeCreditoRepository.findByCiclistaId(anyInt())).thenReturn(Optional.of(cartaoDeCredito));

        CartaoDeCredito result = cartaoDeCreditoService.obterCartaoDeCredito(1);

        assertNotNull(result);
        verify(cartaoDeCreditoRepository).findByCiclistaId(1);
    }

    @Test
    void testObterCartaoDeCreditoNotFound() {
        when(cartaoDeCreditoRepository.findByCiclistaId(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartaoDeCreditoService.obterCartaoDeCredito(1));
    }

    @Test
    void testAlterarCartaoDeCredito() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("Joao Silva");
        ciclista.setEmail("joao.silva@example.com");

        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDeCreditoDTO.setNomeTitular("Joao Silva");
        novoCartaoDeCreditoDTO.setNumero("1234567890123456");
        novoCartaoDeCreditoDTO.setValidade("2025-12-31");
        novoCartaoDeCreditoDTO.setCvv("123");

        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(1);
        when(cartaoDeCreditoRepository.findByCiclistaId(anyInt())).thenReturn(Optional.of(cartaoDeCredito));
        when(administradoraCCService.validarCartao(any(NovoCartaoDeCreditoDTO.class), eq(true))).thenReturn(true);
        when(ciclistaRepository.findById(anyInt())).thenReturn(Optional.of(ciclista));

        cartaoDeCreditoService.alterarCartaoDeCredito(1, novoCartaoDeCreditoDTO);

        verify(cartaoDeCreditoRepository).save(cartaoDeCredito);
        verify(emailService).enviarEmail(any(EmailDTO.class));
    }

    @Test
    void testAlterarCartaoDeCreditoNotFound() {
        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        // Configurando o mock para retornar vazio, simulando que o cartão não foi encontrado
        when(cartaoDeCreditoRepository.findByCiclistaId(anyInt())).thenReturn(Optional.empty());

        // Alterando a expectativa para ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartaoDeCreditoService.alterarCartaoDeCredito(1, novoCartaoDeCreditoDTO);
        });

        // Verificando se a mensagem da exceção é a esperada
        assertEquals("Cartão de crédito não encontrado.", exception.getMessage());
    }


    @Test
    void testValidarCartaoDeCredito() {
        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDeCreditoDTO.setNomeTitular("Nome");
        novoCartaoDeCreditoDTO.setNumero("1234567890123456");
        novoCartaoDeCreditoDTO.setValidade("2025-12-31");
        novoCartaoDeCreditoDTO.setCvv("123");

        when(administradoraCCService.validarCartao(any(NovoCartaoDeCreditoDTO.class), eq(true))).thenReturn(true);

        assertDoesNotThrow(() -> cartaoDeCreditoService.validarCartaoDeCredito(novoCartaoDeCreditoDTO));
    }

    @Test
    void testValidarCartaoDeCreditoInvalid() {
        // Arrange
        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDeCreditoDTO.setNomeTitular("Nome");
        novoCartaoDeCreditoDTO.setNumero("1234567890123456");
        novoCartaoDeCreditoDTO.setValidade("2025-12-31");
        novoCartaoDeCreditoDTO.setCvv("123");

        // Mockando o método para retornar false
        when(administradoraCCService.validarCartao(any(NovoCartaoDeCreditoDTO.class), eq(true))).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> cartaoDeCreditoService.validarCartaoDeCredito(novoCartaoDeCreditoDTO));
    }


    @Test
    void testSave() {
        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        when(cartaoDeCreditoMapper.toEntity(any(NovoCartaoDeCreditoDTO.class))).thenReturn(cartaoDeCredito);

        cartaoDeCreditoService.save(novoCartaoDeCreditoDTO, 1);

        verify(cartaoDeCreditoRepository).save(cartaoDeCredito);
    }
}