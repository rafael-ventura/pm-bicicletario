package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import com.example.bicicletario.bicicletario.mapper.CiclistaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CiclistaServiceTest {

    @Mock
    private CiclistaRepository ciclistaRepository;

    @Mock
    private CiclistaMapper ciclistaMapper;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private AdministradoraCCService administradoraCCService;

    @Mock
    private BicicletaService bicicletaService;

    @Mock
    private CartaoDeCreditoService cartaoDeCreditoService;

    @InjectMocks
    private CiclistaService ciclistaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarCiclista_Success() throws BadRequestException {
        NovoCiclistaDTO novoCiclistaDTO = new NovoCiclistaDTO();
        NovoCartaoDeCreditoDTO meioDePagamentoDTO = new NovoCartaoDeCreditoDTO();
        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(novoCiclistaDTO);
        requestDTO.setMeioDePagamento(meioDePagamentoDTO);

        Ciclista ciclista = new Ciclista();

        when(ciclistaMapper.toEntity(novoCiclistaDTO)).thenReturn(ciclista);
        when(ciclistaRepository.save(ciclista)).thenReturn(ciclista);

        ciclistaService.cadastrarCiclista(requestDTO);

        verify(administradoraCCService).validarCartao(meioDePagamentoDTO, true);
        verify(ciclistaRepository).save(ciclista);
    }

    @Test
    void testObterCiclista_Success() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        Optional<Ciclista> result = ciclistaService.obterCiclista(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testObterCiclista_NotFound() {
        when(ciclistaRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Ciclista> result = ciclistaService.obterCiclista(1);
        assertFalse(result.isPresent());
    }
}
