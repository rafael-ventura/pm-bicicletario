/*
package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.application.services.EmailService;
import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarEmailParaBicicleta_Sucesso() {
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("teste@exemplo.com");

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(1);
        bicicleta.setMarca("Marca A");
        bicicleta.setModelo("Modelo X");
        bicicleta.setAno("2022");

        Tranca tranca = new Tranca();
        tranca.setId(1);

        // Mockando a chamada ao serviço de funcionário
        when(funcionarioService.get(1)).thenReturn(funcionario);

        // Mockando a chamada ao serviço de envio de e-mail
        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        emailService.enviarEmailParaBicicleta(1, bicicleta, tranca, "Inclusão");

        verify(funcionarioService, times(1)).get(1);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmailParaTranca_Sucesso() {
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("teste@exemplo.com");

        Tranca tranca = new Tranca();
        tranca.setNumero(1);
        tranca.setModelo("Modelo X");
        tranca.setAnoDeFabricacao("2022");
        tranca.setLocalizacao("Rua Y, 123");

        // Mockando a chamada ao serviço de funcionário
        when(funcionarioService.get(1)).thenReturn(funcionario);

        // Mockando a chamada ao serviço de envio de e-mail
        doNothing().when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));

        emailService.enviarEmailParaTranca(1, tranca, "Inclusão");

        verify(funcionarioService, times(1)).get(1);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void enviarEmailParaBicicleta_FuncionarioNotFound() {
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        Bicicleta bicicleta = new Bicicleta();
        Tranca tranca = new Tranca();

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaBicicleta(1, bicicleta, tranca, "Inclusão");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }

    @Test
    void enviarEmailParaTranca_FuncionarioNotFound() {
        when(funcionarioService.get(1)).thenThrow(new ResourceNotFoundException("Funcionário não encontrado"));

        Tranca tranca = new Tranca();
        tranca.setNumero(1);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.enviarEmailParaTranca(1, tranca, "Inclusão");
        });

        assertEquals("Funcionário não encontrado", exception.getMessage());
    }
}
*/
