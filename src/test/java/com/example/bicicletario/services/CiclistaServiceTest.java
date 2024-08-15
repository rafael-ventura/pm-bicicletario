package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.PassaporteDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
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

    @InjectMocks
    private CiclistaService ciclistaService;

    @Mock
    private CiclistaMapper ciclistaMapper;

    @Mock
    private CiclistaRepository ciclistaRepository;

    @Mock
    private BicicletaService bicicletaService;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private CartaoDeCreditoService cartaoDeCreditoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarCiclista() {
        // Criando os DTOs
        PassaporteDTO passaporte = new PassaporteDTO();
        passaporte.setNumero("123456");
        passaporte.setValidade("2025-01-01");
        passaporte.setPais("Brasil");

        NovoCartaoDeCreditoDTO cartao = new NovoCartaoDeCreditoDTO();
        cartao.setNomeTitular("Joao Silva");
        cartao.setNumero("1234567890123456");
        cartao.setValidade("2025-01-01");
        cartao.setCvv("123");

        NovoCiclistaDTO novoCiclista = new NovoCiclistaDTO();
        novoCiclista.setNome("Joao Silva");
        novoCiclista.setCpf("12345678900");
        novoCiclista.setEmail("joao.silva@example.com");
        novoCiclista.setNascimento("2000-01-01");
        novoCiclista.setNacionalidade(Nacionalidade.BRASILEIRO);
        novoCiclista.setUrlFotoDocumento("http://example.com/foto.jpg");
        novoCiclista.setPassaporte(passaporte);

        NovoCiclistaRequestDTO dto = new NovoCiclistaRequestDTO();
        dto.setMeioDePagamento(cartao);
        dto.setCiclista(novoCiclista);

        // Criando o objeto Ciclista que será retornado pelo serviço
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("Joao Silva");

        // Mock do mapper
        when(ciclistaMapper.toEntity(any())).thenReturn(ciclista);

        // Mock do método save que não retorna nada
        doNothing().when(cartaoDeCreditoService).save(any(), anyInt());

        // Mock do repositório
        when(ciclistaRepository.save(any())).thenReturn(ciclista);

        // Chamando o método do serviço
        Ciclista resultado = ciclistaService.cadastrarCiclista(dto);

        // Verificando o resultado
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Joao Silva", resultado.getNome());

        // Verificações adicionais
        verify(ciclistaMapper).toEntity(novoCiclista);
        verify(cartaoDeCreditoService).save(cartao, ciclista.getId());
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

    @Test
    void testAlterarCiclista_Success() {
        NovoCiclistaRequestDTO novoCiclistaDTO = new NovoCiclistaRequestDTO();
        NovoCiclistaDTO novoCiclista = new NovoCiclistaDTO();
        novoCiclistaDTO.setCiclista(novoCiclista);
        novoCiclistaDTO.getCiclista().setNome("Updated Name");
        novoCiclistaDTO.getCiclista().setEmail("updated.email@example.com");
        novoCiclistaDTO.getCiclista().setNascimento("2000-01-01");
        novoCiclistaDTO.getCiclista().setNacionalidade(Nacionalidade.BRASILEIRO);
        novoCiclistaDTO.getCiclista().setCpf("12345678900");

        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("Updated Name");

        when(ciclistaRepository.existsById(1)).thenReturn(true);
        when(ciclistaMapper.toEntity(any())).thenReturn(ciclista);
        when(ciclistaRepository.save(any())).thenReturn(ciclista);

        Ciclista updatedCiclista = ciclistaService.alterarCiclista(1, novoCiclistaDTO);

        assertNotNull(updatedCiclista);
        assertEquals(1, updatedCiclista.getId());
        assertEquals("Updated Name", updatedCiclista.getNome());
    }

    @Test
    void testAlterarCiclista_NotFound() {
        NovoCiclistaRequestDTO novoCiclistaDTO = new NovoCiclistaRequestDTO();

        when(ciclistaRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ciclistaService.alterarCiclista(1, novoCiclistaDTO));
    }

    @Test
    void testAtivarCiclista_Success() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(StatusCiclista.INATIVO);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(ciclistaRepository.save(any())).thenReturn(ciclista);

        Ciclista ativado = ciclistaService.ativarCiclista(1);

        assertNotNull(ativado);
        assertEquals(StatusCiclista.ATIVO, ativado.getStatus());
    }

    // testAtivarCiclista_NotFound
    @Test
    void testAtivarCiclista_NotFound() {
        when(ciclistaRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ciclistaService.ativarCiclista(1));
    }

    @Test
    void testPermiteAluguel_Success() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(StatusCiclista.ATIVO);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(1)).thenReturn(false);

        boolean result = ciclistaService.permiteAluguel(1);

        assertTrue(result);
    }

    @Test
    void testPermiteAluguel_Fail() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(StatusCiclista.INATIVO);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        boolean result = ciclistaService.permiteAluguel(1);

        assertFalse(result);
    }

    @Test
    void testObterBicicletaAlugada() {
        // Mock Ciclista
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(StatusCiclista.ATIVO);
        ciclista.setNome("Joao Silva");

        // Mock Aluguel and Bicicleta
        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        aluguel.setBicicleta(bicicleta.getId());

        // Mock repository methods
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.findByCiclistaAndHoraFimIsNull(1)).thenReturn(Optional.of(aluguel));
        when(bicicletaService.getBicicleta()).thenReturn(Optional.of(bicicleta));

        // Call service method
        Optional<Bicicleta> bicicletaAlugada = ciclistaService.obterBicicletaAlugada(1);

        // Assertions
        assertTrue(bicicletaAlugada.isPresent());
        assertEquals(1, bicicletaAlugada.get().getId());
    }

    @Test
    void testExisteEmail_Success() {
        when(ciclistaRepository.existsByEmail("joao.silva@example.com")).thenReturn(true);

        boolean result = ciclistaService.existeEmail("joao.silva@example.com");

        assertTrue(result);
    }

    @Test
    void testExisteEmail_Fail() {
        when(ciclistaRepository.existsByEmail("joao.silva@example.com")).thenReturn(false);

        boolean result = ciclistaService.existeEmail("joao.silva@example.com");

        assertFalse(result);
    }

}
