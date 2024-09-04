package com.example.bicicletario.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class BicicletaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BicicletaService bicicletaService;

    private final String baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8020/api";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Injetando o valor do baseUrl no serviço manualmente
        try {
            var field = BicicletaService.class.getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(bicicletaService, baseUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBicicletaById_Success() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenReturn(new ResponseEntity<>(bicicleta, HttpStatus.OK));

        // Act
        Bicicleta result = bicicletaService.getBicicletaById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void getBicicletaById_NotFound() {
        // Arrange
        when(restTemplate.getForEntity(eq(baseUrl + "/bicicleta/1"), eq(Bicicleta.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bicicletaService.getBicicletaById(5555));
    }

   /* @Test
    void atualizarStatus_Success() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        String expectedUrl = baseUrl + "/bicicleta/" + bicicleta.getId() + "/status/DISPONIVEL";

        when(restTemplate.exchange(
                eq(expectedUrl), // Verifique que a URL é a esperada
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Bicicleta.class))
        ).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.DISPONIVEL);

        // Assert
        verify(restTemplate, times(1)).exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Bicicleta.class)
        );
    }

    @Test
    void atualizarStatus_Exception() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        // Simulando que o RestTemplate lança uma HttpClientErrorException$NotFound
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .when(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Bicicleta.class));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.DISPONIVEL));
    }*/
}

