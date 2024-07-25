package com.example.bicicletario.Services;

import com.example.bicicletario.bicicletario.application.CobrancaService;
import com.example.bicicletario.bicicletario.application.FilaCobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.FilaCobrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FilaCobrancaServiceTest {

    @InjectMocks
    private FilaCobrancaService filaCobrancaService;

    @Mock
    private FilaCobrancaRepository filaCobrancaRepository;

    @Mock
    private CobrancaService cobrancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adicionarNaFilaComSucesso() {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();
        novaCobrancaDTO.setCiclista(1);
        novaCobrancaDTO.setValor(BigDecimal.TEN);

        Cobranca result = filaCobrancaService.adicionarNaFila(novaCobrancaDTO);

        assertNotNull(result);
        assertEquals(StatusCobranca.PENDENTE, result.getStatusCobranca());
        verify(filaCobrancaRepository, times(1)).adicionarNaFila(result);
    }

    @Test
    void adicionarNaFilaComValorInvalido() {
        NovoCobrancaDTO novaCobrancaDTO = new NovoCobrancaDTO();
        novaCobrancaDTO.setCiclista(1);
        novaCobrancaDTO.setValor(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            filaCobrancaService.adicionarNaFila(novaCobrancaDTO);
        });

        assertEquals("O valor deve ser maior que zero", exception.getMessage());
    }

    @Test
    void processarFilaComSucesso() {
        Cobranca cobranca1 = new Cobranca();
        cobranca1.setCiclista(1);
        cobranca1.setValor(BigDecimal.TEN);
        cobranca1.setStatusCobranca(StatusCobranca.PENDENTE);

        Cobranca cobranca2 = new Cobranca();
        cobranca2.setCiclista(2);
        cobranca2.setValor(BigDecimal.TEN);
        cobranca2.setStatusCobranca(StatusCobranca.PENDENTE);

        Queue<Cobranca> fila = new LinkedList<>();
        fila.add(cobranca1);
        fila.add(cobranca2);

        when(filaCobrancaRepository.isEmpty()).thenAnswer(invocation -> fila.isEmpty());
        when(filaCobrancaRepository.removerDaFila()).thenAnswer(invocation -> fila.poll());

        when(cobrancaService.realizarCobranca(any(NovoCobrancaDTO.class))).thenAnswer(invocation -> {
            NovoCobrancaDTO dto = invocation.getArgument(0);
            Cobranca cobranca = new Cobranca();
            cobranca.setCiclista(dto.getCiclista());
            cobranca.setValor(dto.getValor());
            cobranca.setStatusCobranca(StatusCobranca.PAGA);
            return cobranca;
        });

        List<Cobranca> cobrancasProcessadas = filaCobrancaService.processarFila();

        assertEquals(2, cobrancasProcessadas.size());
        assertTrue(cobrancasProcessadas.stream().anyMatch(c -> c.getCiclista() == 1));
        assertTrue(cobrancasProcessadas.stream().anyMatch(c -> c.getCiclista() == 2));
    }
}
