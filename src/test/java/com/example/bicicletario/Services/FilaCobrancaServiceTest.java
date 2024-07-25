package com.example.bicicletario.Services;

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
import static org.mockito.Mockito.*;

class FilaCobrancaServiceTest {

    @InjectMocks
    private FilaCobrancaService filaCobrancaService;

    @Mock
    private FilaCobrancaRepository filaCobrancaRepository;

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
        cobranca1.setValor(BigDecimal.TEN);
        cobranca1.setStatusCobranca(StatusCobranca.PENDENTE);

        Cobranca cobranca2 = new Cobranca();
        cobranca2.setValor(BigDecimal.TEN);
        cobranca2.setStatusCobranca(StatusCobranca.PENDENTE);

        Queue<Cobranca> fila = new LinkedList<>();
        fila.add(cobranca1);
        fila.add(cobranca2);

        when(filaCobrancaRepository.isEmpty()).thenAnswer(invocation -> fila.isEmpty());
        when(filaCobrancaRepository.removerDaFila()).thenAnswer(invocation -> fila.poll());

        List<Cobranca> cobrancasProcessadas = filaCobrancaService.processarFila();

        assertEquals(2, cobrancasProcessadas.size());
        assertEquals(StatusCobranca.PAGA, cobrancasProcessadas.get(0).getStatusCobranca());
        assertEquals(StatusCobranca.PAGA, cobrancasProcessadas.get(1).getStatusCobranca());
    }

    @Test
    void processarFilaComFalha() {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(BigDecimal.TEN);
        cobranca.setStatusCobranca(StatusCobranca.PENDENTE);

        Queue<Cobranca> fila = new LinkedList<>();
        fila.add(cobranca);

        when(filaCobrancaRepository.isEmpty()).thenAnswer(invocation -> fila.isEmpty());
        when(filaCobrancaRepository.removerDaFila()).thenAnswer(invocation -> fila.poll());

        FilaCobrancaService filaCobrancaServiceSpy = spy(filaCobrancaService);
        doReturn(false).when(filaCobrancaServiceSpy).enviarParaAdministradoraCC(any(Cobranca.class));

        List<Cobranca> cobrancasProcessadas = filaCobrancaServiceSpy.processarFila();

        assertEquals(1, cobrancasProcessadas.size());
        assertEquals(StatusCobranca.FALHA, cobrancasProcessadas.get(0).getStatusCobranca());
    }
}
