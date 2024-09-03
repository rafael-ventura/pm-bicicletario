package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.application.external.TrancaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Passaporte;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import com.example.bicicletario.bicicletario.domain.mapper.CiclistaMapper;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
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
    private EmailService emailService;

    // Mock do BicicletaService
    @Mock
    private BicicletaService bicicletaService;

    @Mock
    private TrancaService trancaService;

    @Mock
    private CiclistaMapper ciclistaMapper;

    @Mock
    private CiclistaRepository ciclistaRepository;

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
        Passaporte passaporte = new Passaporte();
        passaporte.setNumero("123456");
        passaporte.setValidade("2025-01-01");
        passaporte.setPais("Brasil");

        NovoCartaoDeCreditoDTO cartao = new NovoCartaoDeCreditoDTO();
        cartao.setNomeTitular("Joao Silva");
        cartao.setNumero("1234567890123456");
        cartao.setValidade("2025-01-01");
        cartao.setCvv("123");

        Ciclista novoCiclista = new Ciclista();
        novoCiclista.setNome("Joao Silva");
        novoCiclista.setCpf("12345678900");
        novoCiclista.setEmail("joao.silva@example.com");
        novoCiclista.setNascimento("2000-01-01");
        novoCiclista.setNacionalidade(Nacionalidade.BRASILEIRO);
        novoCiclista.setUrlFotoDocumento("http://example.com/foto.jpg");
        novoCiclista.setPassaporte(passaporte);
        novoCiclista.setSenha("minhaSenhaSecreta");
        novoCiclista.setConfirmacaoSenha("minhaSenhaSecreta");

        NovoCiclistaRequestDTO dto = new NovoCiclistaRequestDTO();
        dto.setMeioDePagamento(cartao);
        dto.setCiclista(novoCiclista);

        // Criando o objeto Ciclista que será retornado pelo serviço
        Ciclista ciclistaSalvo = new Ciclista();
        ciclistaSalvo.setId(1); // Definindo o ID para evitar o NPE
        ciclistaSalvo.setNome("Joao Silva");

        // Mock
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
    void testAlterarCiclista_NotFound() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNome("Name");
        NovoCiclistaDTO ciclistaDTO = new NovoCiclistaDTO();
        ciclistaDTO.setNome("Updated Name");

        when(ciclistaRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ciclistaService.alterarCiclista(1, ciclistaDTO));
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
        int idCiclista = 1;
        // Mock Ciclista
        Ciclista ciclista = new Ciclista();
        ciclista.setId(idCiclista);
        ciclista.setStatus(StatusCiclista.ATIVO);
        ciclista.setNome("Joao Silva");

        // Mock Aluguel and Bicicleta
        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        aluguel.setBicicleta(bicicleta.getId());

        // Mock repository methods
        when(ciclistaRepository.findById(idCiclista)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.findByCiclistaAndHoraFimIsNull(idCiclista)).thenReturn(Optional.of(aluguel));
        when(bicicletaService.getBicicletaById(bicicleta.getId())).thenReturn(bicicleta);

        // Call service method
        Optional<Bicicleta> bicicletaAlugada = ciclistaService.obterBicicletaAlugada(idCiclista);

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

    @Test
    void testValidarCamposObrigatorios() {
        Ciclista ciclistaInvalido = new Ciclista(); // Ciclista com dados faltando

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclistaInvalido);

        assertThrows(BadRequestException.class, () -> ciclistaService.cadastrarCiclista(requestDTO));
    }

    @Test
    void testValidarCpfInvalido() {
        // Configurando ciclista com todos os campos obrigatórios, mas com CPF inválido
        Ciclista ciclistaComCpfInvalido = new Ciclista();
        ciclistaComCpfInvalido.setNome("Joao Silva");
        ciclistaComCpfInvalido.setEmail("joao.silva@example.com");
        ciclistaComCpfInvalido.setNascimento("2000-01-01");
        ciclistaComCpfInvalido.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaComCpfInvalido.setCpf("123");  // CPF inválido

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclistaComCpfInvalido);

        // Agora verificamos se a exceção InvalidDataException é lançada
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> ciclistaService.cadastrarCiclista(requestDTO));
        assertEquals("CPF inválido. O CPF deve conter 11 dígitos e apenas números.", exception.getMessage());
    }


    @Test
    void testEnviarEmailComFalha() {
        Passaporte passaporte = new Passaporte();
        passaporte.setNumero("123456");

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Joao Silva");
        ciclista.setCpf("12345678900");
        ciclista.setEmail("joao.silva@example.com");
        ciclista.setPassaporte(passaporte);
        ciclista.setSenha("senhaSegura");

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclista);

        doThrow(new RuntimeException()).when(emailService).enviarEmailConfirmacao(any());

        assertThrows(BadRequestException.class, () -> ciclistaService.cadastrarCiclista(requestDTO));
    }

    @Test
    void testObterBicicletaAlugadaSemAluguel() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.findByCiclistaAndHoraFimIsNull(1)).thenReturn(Optional.empty());

        Optional<Bicicleta> bicicletaAlugada = ciclistaService.obterBicicletaAlugada(1);

        assertFalse(bicicletaAlugada.isPresent());
    }

    // Verificação de nome nulo
    @Test
    void testCadastrarCiclistaNomeNulo() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome(null);  // Nome nulo
        ciclista.setEmail("joao.silva@example.com");
        ciclista.setCpf("12345678900");
        ciclista.setNascimento("2000-01-01");

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclista);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> ciclistaService.cadastrarCiclista(requestDTO));
        assertEquals("Todos os campos são obrigatórios.", exception.getMessage());
    }

    // Verificação de CPF vazio
    @Test
    void testCadastrarCiclistaCpfVazio() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Joao Silva");
        ciclista.setEmail("joao.silva@example.com");
        ciclista.setCpf("");  // CPF vazio
        ciclista.setNascimento("2000-01-01");

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclista);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> ciclistaService.cadastrarCiclista(requestDTO));
        assertEquals("Todos os campos são obrigatórios.", exception.getMessage());
    }

    // Verificação de passaporte para estrangeiros
    @Test
    void testCadastrarCiclistaPassaporteNuloParaEstrangeiro() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Joao Silva");
        ciclista.setEmail("joao.silva@example.com");
        ciclista.setNacionalidade(Nacionalidade.ESTRANGEIRO);
        ciclista.setId(1);
        ciclista.setCpf("12345678900");
        ciclista.setNascimento("2000-01-01");
        ciclista.setConfirmacaoSenha("minhaSenhaSecreta");
        ciclista.setSenha("minhaSenhaSecreta");
        ciclista.setStatus(StatusCiclista.INATIVO);
        ciclista.setUrlFotoDocumento("http://example.com/foto.jpg");
        ciclista.setPassaporte(null);  // Passaporte nulo

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclista);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> ciclistaService.cadastrarCiclista(requestDTO));
        assertEquals("Passaporte é obrigatório para estrangeiros.", exception.getMessage());
    }

    // Verificar envio de email
    @Test
    void testVerificarEnvioEmailConfirmacao() {
        var ciclista = mock(Ciclista.class);

        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        requestDTO.setCiclista(ciclista);

        // Mock para garantir que o email é enviado
        ciclistaService.cadastrarCiclista(requestDTO);
        verify(emailService, times(1)).enviarEmailConfirmacao(any());
    }


    @Test
    void ativarCiclista_JaAtivo() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(StatusCiclista.ATIVO);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            ciclistaService.ativarCiclista(1);
        });

        assertEquals("Ciclista já está ativo.", exception.getMessage());
    }

    @Test
    void cadastrarCiclista_CpfInvalido() {
        // Arrange
        NovoCiclistaRequestDTO requestDTO = new NovoCiclistaRequestDTO();
        Ciclista ciclista = new Ciclista();
        ciclista.setCpf("123"); // CPF inválido
        ciclista.setNome("Joao Silva");
        ciclista.setEmail("vito@gmail.com");
        ciclista.setNascimento("2000-01-01");
        ciclista.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclista.setUrlFotoDocumento("http://example.com/foto.jpg");
        ciclista.setSenha("minhaSenhaSecreta");
        ciclista.setConfirmacaoSenha("minhaSenhaSecreta");
        requestDTO.setCiclista(ciclista);

        // Act & Assert
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            ciclistaService.cadastrarCiclista(requestDTO);
        });

        assertEquals("CPF inválido. O CPF deve conter 11 dígitos e apenas números.", exception.getMessage());
    }

    @Test
    void cadastrarCiclista_Success() {
        // Arrange
        var requestDTO = mock(NovoCiclistaRequestDTO.class);
        var ciclista = mock(Ciclista.class);

        when(ciclistaRepository.save(any(Ciclista.class))).thenReturn(ciclista);
        // Act
        Ciclista result = ciclistaService.cadastrarCiclista(requestDTO);

        // Assert
        assertNotNull(result);
        verify(ciclistaRepository, times(1)).save(any(Ciclista.class));
        assertEquals(ciclista, result);
    }

    @Test
    void permiteAluguel_CiclistaInativo() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setStatus(StatusCiclista.INATIVO);

        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        // Act
        boolean result = ciclistaService.permiteAluguel(1);

        // Assert
        assertFalse(result);
    }

}
