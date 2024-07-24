package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
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
        novoCiclistaDTO.setNome("Ciclista");
        novoCiclistaDTO.setEmail("ciclista@gmail.com");
        novoCiclistaDTO.setCpf("12345678901");
        novoCiclistaDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        novoCiclistaDTO.setNascimento("01/01/2000");
        novoCiclistaDTO.setUrlFotoDocumento("http://example.com/foto.jpg");
        novoCiclistaDTO.setSenha("123456");
        novoCiclistaDTO.setConfirmacaoSenha("123456");

        NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO = new NovoCartaoDeCreditoDTO();
        novoCartaoDeCreditoDTO.setNumero("1234567890123456");
        novoCartaoDeCreditoDTO.setNomeTitular("Ciclista");
        novoCartaoDeCreditoDTO.setValidade("01/25");
        novoCartaoDeCreditoDTO.setCvv("123");

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(novoCiclistaDTO);
        requestDTO.setMeioDePagamento(novoCartaoDeCreditoDTO);

        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("Ciclista");
        ciclista.setEmail("ciclista@gmail.com");
        ciclista.setCpf("12345678901");
        ciclista.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclista.setStatusCiclista(StatusCiclista.ATIVO);
        ciclista.setNascimento("01/01/2000");
        ciclista.setUrlFotoDocumento("http://example.com/foto.jpg");

        when(ciclistaMapper.toEntity(novoCiclistaDTO)).thenReturn(ciclista);
        when(ciclistaRepository.save(ciclista)).thenReturn(ciclista);

        ciclistaService.cadastrarCiclista(requestDTO);

        verify(administradoraCCService).validarCartao(novoCartaoDeCreditoDTO, true);
        verify(ciclistaRepository).save(ciclista);
    }

    @Test
    void testObterCiclista_Success() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        Optional<Ciclista> result = ciclistaService.obterCiclista(1);

        assertTrue(result.isPresent());
        assertEquals(ciclista, result.get());
    }

    @Test
    void testObterCiclista_NotFound() {
        when(ciclistaRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ciclistaService.obterCiclista(1));
    }

}
