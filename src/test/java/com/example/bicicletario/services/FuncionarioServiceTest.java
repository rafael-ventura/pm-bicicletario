package com.example.bicicletario.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.FuncionarioRepository;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

public class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FuncionarioMapper funcionarioMapper;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private final ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(consoleContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testCadastrarFuncionario() {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("Joao Silva");
        dto.setCpf("12345678900");
        dto.setEmail("joao@gmail.com");
        dto.setSenha("123456");
        dto.setConfirmacaoSenha("123456");
        dto.setIdade(25);
        dto.setFuncao("DEV");

        Funcionario funcionario = new Funcionario();

        // Configura o mock para retornar o objeto funcionario quando toEntity é chamado
        when(funcionarioMapper.toEntity(dto)).thenReturn(funcionario);
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> {
            Funcionario savedFuncionario = invocation.getArgument(0);
            savedFuncionario.setMatricula("MAT-1234");
            return savedFuncionario;
        });

        Funcionario result = funcionarioService.cadastrarFuncionario(dto);

        assertNotNull(result);
        assertEquals("MAT-", result.getMatricula().substring(0, 4));

        // Use ArgumentCaptor to capture the argument passed to save method
        ArgumentCaptor<Funcionario> funcionarioCaptor = ArgumentCaptor.forClass(Funcionario.class);
        verify(funcionarioRepository, times(1)).save(funcionarioCaptor.capture());

        // Verify the properties of the captured Funcionario object
        Funcionario capturedFuncionario = funcionarioCaptor.getValue();
        assertEquals("Joao Silva", capturedFuncionario.getNome());
        assertEquals("12345678900", capturedFuncionario.getCpf());
        assertEquals("joao@gmail.com", capturedFuncionario.getEmail());
        assertEquals("123456", capturedFuncionario.getSenha());
        assertEquals(25, capturedFuncionario.getIdade());
        assertEquals("DEV", capturedFuncionario.getFuncao());
    }

    @Test
    public void testCadastrarFuncionarioCpfInvalido() {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("Joao Silva");
        dto.setCpf("123.456.789-00");
        dto.setEmail("joao@gmail.com");
        dto.setSenha("123456");
        dto.setConfirmacaoSenha("123456");
        dto.setIdade(25);
        dto.setFuncao("DEV...");

        assertThrows(InvalidDataException.class, () -> {
            funcionarioService.cadastrarFuncionario(dto);
        });
    }

    @Test
    public void testCadastrarFuncionarioSenhaDiferente() {
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("Joao Silva");
        dto.setCpf("123.456.789-00");
        dto.setEmail("joao@gmail.com");
        dto.setSenha("123456");
        dto.setConfirmacaoSenha("1234567");
        dto.setIdade(25);
        dto.setFuncao("DEV...");

        assertThrows(InvalidDataException.class, () -> {
            funcionarioService.cadastrarFuncionario(dto);
        });
    }

    @Test
    public void testExcluirFuncionario() {
        Integer idFuncionario = 1;
        Funcionario funcionario = new Funcionario();
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.of(funcionario));

        funcionarioService.excluirFuncionario(idFuncionario);

        verify(funcionarioRepository, times(1)).delete(funcionario);
    }

    @Test
    public void testExcluirFuncionarioNaoEncontrado() {
        Integer idFuncionario = 1;
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.excluirFuncionario(idFuncionario);
        });
    }

    @Test
    public void testAlterarFuncionario() {
        // ID do funcionário a ser alterado
        Integer idFuncionario = 1;

        // Funcionario existente
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario); // Certifique-se de que o ID está configurado corretamente
        funcionario.setNome("Joao Silva");
        funcionario.setCpf("12345678900");
        funcionario.setEmail("joao-antigo@gmail.com");
        funcionario.setSenha("123456");
        funcionario.setIdade(25);
        funcionario.setFuncao("DEV...");
        funcionario.setMatricula("MAT-0001");

        // NovoFuncionarioDTO com dados atualizados
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setNome("Joao Silva");
        dto.setEmail("joao@gmail.com");
        dto.setCpf("12345678900");
        dto.setSenha("123456");
        dto.setConfirmacaoSenha("123456");
        dto.setIdade(25);
        dto.setFuncao("DEV...");

        // Configurando o mock do repositório para retornar o funcionário existente
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.of(funcionario));

        // Configurando o mock do método save para atualizar os dados do funcionário
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> {
            Funcionario savedFuncionario = invocation.getArgument(0);
            savedFuncionario.setNome(dto.getNome());
            savedFuncionario.setEmail(dto.getEmail());
            savedFuncionario.setCpf(dto.getCpf());
            savedFuncionario.setSenha(dto.getSenha());
            savedFuncionario.setIdade(dto.getIdade());
            savedFuncionario.setFuncao(dto.getFuncao());
            return savedFuncionario;
        });

        // Chamando o método do serviço
        NovoFuncionarioDTO result = funcionarioService.alterarFuncionario(idFuncionario, dto);

        // Verificando o resultado
        assertNotNull(result);
        assertEquals("Joao Silva", result.getNome());
        assertEquals("joao@gmail.com", result.getEmail());
        assertEquals("12345678900", result.getCpf());
        assertEquals("123456", result.getSenha());
        assertEquals(25, result.getIdade());
        assertEquals("DEV...", result.getFuncao());

        // Verificando as interações com os mocks
        verify(funcionarioRepository, times(1)).findById(idFuncionario);
        verify(funcionarioRepository, times(1)).save(funcionario);
    }

    @Test
    public void testAlterarFuncionarioNaoEncontrado() {
        Integer idFuncionario = 1;
        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.alterarFuncionario(idFuncionario, dto);
        });
    }

    @Test
    public void testListarFuncionarios() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Joao Silva");
        funcionario.setCpf("12345678900");
        funcionario.setEmail("joao@gmail.com");
        funcionario.setSenha("123456");
        funcionario.setIdade(25);
        funcionario.setFuncao("DEV...");
        funcionario.setMatricula("MAT-0001");
        when(funcionarioRepository.findAll()).thenReturn(List.of(funcionario));
        when(funcionarioMapper.toDtoList(List.of(funcionario))).thenReturn(List.of(new NovoFuncionarioDTO()));

        List<NovoFuncionarioDTO> result = funcionarioService.listarFuncionarios();
        assertEquals(1, result.size());
    }

    @Test
    public void testObterFuncionario() {
        Integer idFuncionario = 1;
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario);
        funcionario.setNome("Joao Silva");
        funcionario.setCpf("12345678900");
        funcionario.setEmail("joao@gmail.com");
        funcionario.setSenha("123456");
        funcionario.setIdade(25);
        funcionario.setFuncao("DEV...");

        NovoFuncionarioDTO dto = new NovoFuncionarioDTO();
        dto.setFuncao("DEV...");
        dto.setIdade(25);
        dto.setNome("Joao Silva");
        dto.setCpf("12345678900");
        dto.setEmail("joao@gmail.com");
        dto.setSenha("123456");
        dto.setConfirmacaoSenha("123456");

        when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.of(funcionario));
        when(funcionarioMapper.toDto(funcionario)).thenReturn(dto);

        NovoFuncionarioDTO result = funcionarioService.obterFuncionario(idFuncionario);
        assertNotNull(result);
    }

    @Test
    public void testObterFuncionarioNaoEncontrado() {
        Integer idFuncionario = 1;
        // Utiliza lenient() para permitir stubbings não utilizados
        lenient().when(funcionarioRepository.findById(idFuncionario)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.obterFuncionario(idFuncionario);
        });
    }
}
