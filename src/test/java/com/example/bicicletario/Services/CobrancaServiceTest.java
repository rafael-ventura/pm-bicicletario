package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.CobrancaService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.CobrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CobrancaServiceTest {

    @InjectMocks
    private CobrancaService cobrancaService;

    @Mock
    private CobrancaRepository cobrancaRepository;

    @Mock
    private AdministradoraCCService administradoraCCService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obterCobrancaPorId() {
        Cobranca cobranca = new Cobranca();
        when(cobrancaRepository.findById(1)).thenReturn(cobranca);

        Cobranca result = cobrancaService.obterCobrancaPorId(1);
        assertEquals(cobranca, result);
    }

    @Test
    void realizarCobrancaComSucesso() {
        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor(BigDecimal.TEN);

        when(administradoraCCService.enviarParaAdministradoraCC(any(CartaoDeCredito.class), any(BigDecimal.class))).thenReturn(true);
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cobranca result = cobrancaService.realizarCobranca(novaCobranca);

        assertNotNull(result);
        assertEquals(StatusCobranca.PAGA, result.getStatusCobranca());
        verify(cobrancaRepository, times(1)).save(any(Cobranca.class));
    }

    @Test
    void realizarCobrancaComFalha() {
        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor(BigDecimal.TEN);

        when(administradoraCCService.enviarParaAdministradoraCC(any(CartaoDeCredito.class), any(BigDecimal.class))).thenReturn(false);
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cobranca result = cobrancaService.realizarCobranca(novaCobranca);

        assertNotNull(result);
        assertEquals(StatusCobranca.FALHA, result.getStatusCobranca());
        verify(cobrancaRepository, times(1)).save(any(Cobranca.class));
    }

    @Test
    void realizarCobrancaComValorInvalido() {
        NovoCobrancaDTO novaCobranca = new NovoCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cobrancaService.realizarCobranca(novaCobranca);
        });

        assertEquals("O valor deve ser maior que zero", exception.getMessage());
    }
}
