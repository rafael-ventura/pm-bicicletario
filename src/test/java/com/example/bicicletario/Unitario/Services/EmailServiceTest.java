package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.EmailService;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FuncionarioService funcionarioService;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarEmailComSucesso() {
        // Arrange
        String email = "teste@exemplo.com";
        String assunto = "Assunto Teste";
        String mensagem = "Mensagem de Teste";

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);

        // Act
        emailService.enviarEmail(email, assunto, mensagem);

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailFalhaBadRequest() {
        // Arrange
        String email = "teste@exemplo.com";
        String assunto = "Assunto Teste";
        String mensagem = "Mensagem de Teste";

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailService.enviarEmail(email, assunto, mensagem);
        });

        assertEquals("Erro ao enviar e-mail", exception.getMessage());
    }

    @Test
    void enviarEmailFalhaNotFound() {
        // Arrange
        String email = "teste@exemplo.com";
        String assunto = "Assunto Teste";
        String mensagem = "Mensagem de Teste";

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            emailService.enviarEmail(email, assunto, mensagem);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void enviarEmailFormatoInvalido() {
        // Arrange
        String email = "teste@exemplo.com";
        String assunto = "Assunto Teste";
        String mensagem = "Mensagem de Teste";

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        // Act & Assert
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            emailService.enviarEmail(email, assunto, mensagem);
        });

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    }

    // Novo teste para exceções inesperadas
    @Test
    void enviarEmailExcecaoGenerica() {
        // Arrange
        String email = "teste@exemplo.com";
        String assunto = "Assunto Teste";
        String mensagem = "Mensagem de Teste";

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            emailService.enviarEmail(email, assunto, mensagem);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    void enviarEmailParaReparador_FuncionarioNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaReparador(1, "Assunto", "Mensagem");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }


    @Test
    void enviarEmailParaBicicletaComFuncionarioNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        Tranca tranca = new Tranca();
        tranca.setId(2);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaBicicleta(1, bicicleta, tranca, "Inclusão");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }

    @Test
    void enviarEmailParaBicicleta_DeveEnviarEmailComSucesso() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(1);
        bicicleta.setMarca("Marca");
        bicicleta.setModelo("Modelo");
        bicicleta.setAno("2020");
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);

        Tranca tranca = new Tranca();
        tranca.setId(2);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);

        when(funcionarioService.get(1)).thenReturn(funcionario);

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);

        // Act
        emailService.enviarEmailParaBicicleta(1, bicicleta, tranca, "Inclusão");

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailParaTranca_DeveEnviarEmailComSucesso() {
        // Arrange
        Tranca tranca = new Tranca();
        tranca.setNumero(1);
        tranca.setModelo("Modelo");
        tranca.setAnoDeFabricacao("2020");
        tranca.setLocalizacao("Localização");
        tranca.setStatus(StatusTranca.LIVRE);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);

        when(funcionarioService.get(1)).thenReturn(funcionario);

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);

        // Act
        emailService.enviarEmailParaTranca(1, tranca, "Inclusão");

        // Assert
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void enviarEmailParaTrancaComFuncionarioNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        Tranca tranca = new Tranca();
        tranca.setNumero(1);
        tranca.setStatus(StatusTranca.LIVRE); // Certificando-se de que o status não é null

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaTranca(1, tranca, "Inclusão");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }


}
