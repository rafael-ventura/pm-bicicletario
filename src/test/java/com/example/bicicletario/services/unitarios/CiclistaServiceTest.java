package com.example.bicicletario.services.unitarios;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.Constants;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
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
    void cadastrarCiclista_Success() {
        // Arrange
        NovoCiclistaRequestDTO request = new NovoCiclistaRequestDTO();

        // Preencher todos os campos obrigatórios no NovoCiclistaDTO
        NovoCiclistaDTO novoCiclistaDTO = new NovoCiclistaDTO();
        novoCiclistaDTO.setEmail("newemail@example.com");
        novoCiclistaDTO.setNome("Novo Nome");
        novoCiclistaDTO.setCpf("12345678900");
        novoCiclistaDTO.setNascimento("1990-01-01");
        novoCiclistaDTO.setNacionalidade(Nacionalidade.BRASILEIRO);

        request.setCiclista(novoCiclistaDTO);

        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);

        // Simular os comportamentos do Mapper e do Repositório
        when(ciclistaMapper.toEntity(novoCiclistaDTO)).thenReturn(ciclista);
        when(ciclistaRepository.save(any(Ciclista.class))).thenReturn(ciclista);

        // Act
        Ciclista result = ciclistaService.cadastrarCiclista(request);

        // Assert
        assertNotNull(result);
        assertEquals(ciclista.getId(), result.getId());
        verify(cartaoDeCreditoService).save(request.getMeioDePagamento(), ciclista.getId());
        verify(ciclistaRepository).save(ciclista);
    }


    @Test
    void cadastrarCiclista_InvalidData() {
        // Arrange
        NovoCiclistaRequestDTO request = new NovoCiclistaRequestDTO();
        NovoCiclistaDTO novoCiclistaDTO = new NovoCiclistaDTO(); // Dados inválidos (faltando email, nome, etc.)
        request.setCiclista(novoCiclistaDTO);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            ciclistaService.cadastrarCiclista(request);
        });

        assertEquals("Todos os campos são obrigatórios.", exception.getMessage());
    }

    @Test
    void obterCiclista_Success() {
        // Arrange
        int idCiclista = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.of(ciclista));

        // Act
        Optional<Ciclista> result = ciclistaService.obterCiclista(idCiclista);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(ciclista, result.get());
    }

    @Test
    void obterCiclista_NotFound() {
        // Arrange
        int idCiclista = 1;
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ciclistaService.obterCiclista(idCiclista);
        });

        assertEquals(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista, exception.getMessage());
    }

    @Test
    void alterarCiclista_Success() {
        // Arrange
        int idCiclista = 1;

        // Inicialize o NovoCiclistaDTO e configure-o corretamente
        NovoCiclistaDTO novoCiclista = new NovoCiclistaDTO();
        novoCiclista.setEmail("joao@example.com");
        novoCiclista.setNome("Joao Alterado");
        novoCiclista.setCpf("12345678900");
        novoCiclista.setNascimento("1990-01-01");
        novoCiclista.setNacionalidade(Nacionalidade.BRASILEIRO);

        // Inicialize o NovoCiclistaRequestDTO e configure-o
        NovoCiclistaRequestDTO novoCiclistaDTO = new NovoCiclistaRequestDTO();
        novoCiclistaDTO.setCiclista(novoCiclista);

        // Simular o objeto Ciclista
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        ciclista.setEmail(novoCiclista.getEmail());
        ciclista.setNome(novoCiclista.getNome());
        ciclista.setCpf(novoCiclista.getCpf());

        // Simulando o comportamento dos mocks
        when(ciclistaRepository.existsById(idCiclista)).thenReturn(true);
        when(ciclistaMapper.toEntity(novoCiclista)).thenReturn(ciclista);
        when(ciclistaRepository.save(any(Ciclista.class))).thenReturn(ciclista);

        // Act
        Ciclista result = ciclistaService.alterarCiclista(idCiclista, novoCiclistaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(ciclista.getId(), result.getId());
        verify(ciclistaRepository).save(ciclista);
    }

    @Test
    void alterarCiclista_NotFound() {
        // Arrange
        int idCiclista = 1;
        NovoCiclistaRequestDTO novoCiclistaDTO = new NovoCiclistaRequestDTO();
        when(ciclistaRepository.existsById(idCiclista)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ciclistaService.alterarCiclista(idCiclista, novoCiclistaDTO);
        });

        assertEquals("Ciclista não encontrado com o ID: " + idCiclista, exception.getMessage());
    }

    @Test
    void ativarCiclista_Success() {
        // Arrange
        int idCiclista = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        ciclista.setStatus(StatusCiclista.INATIVO); // Status inicial

        // Simulando a busca do ciclista no repositório
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.of(ciclista));

        // Simulando o comportamento do método save para retornar o ciclista com status atualizado
        when(ciclistaRepository.save(ciclista)).thenReturn(ciclista);

        // Act
        Ciclista result = ciclistaService.ativarCiclista(idCiclista);

        // Assert
        assertNotNull(result);
        assertEquals(StatusCiclista.ATIVO, result.getStatus()); // Verifica se o status foi atualizado
        verify(ciclistaRepository).save(ciclista); // Verifica se o método save foi chamado
    }


    @Test
    void ativarCiclista_NotFound() {
        // Arrange
        int idCiclista = 1;
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ciclistaService.ativarCiclista(idCiclista);
        });

        assertEquals(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista, exception.getMessage());
    }

    @Test
    void permiteAluguel_Success() {
        // Arrange
        int idCiclista = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        ciclista.setStatus(StatusCiclista.ATIVO);
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(false);

        // Act
        boolean result = ciclistaService.permiteAluguel(idCiclista);

        // Assert
        assertTrue(result);
    }

    @Test
    void permiteAluguel_Fail() {
        // Arrange
        int idCiclista = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        ciclista.setStatus(StatusCiclista.ATIVO);
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(true);

        // Act
        boolean result = ciclistaService.permiteAluguel(idCiclista);

        // Assert
        assertFalse(result);
    }

    @Test
    void obterBicicletaAlugada_Success() {
        // Arrange
        int idCiclista = 1;
        int idTranca = 1;
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        Aluguel aluguel = new Aluguel();
        aluguel.setBicicleta(1); // ID da bicicleta alugada
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        // Mockando os repositórios e serviços
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.findByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(Optional.of(aluguel));
        when(bicicletaService.getBicicletaByTranca(idTranca)).thenReturn(Optional.of(bicicleta));

        // Act
        Optional<Bicicleta> result = ciclistaService.obterBicicletaAlugada(idCiclista);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bicicleta, result.get());
    }

}
