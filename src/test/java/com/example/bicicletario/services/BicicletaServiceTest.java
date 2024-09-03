package com.example.bicicletario.services;

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
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BicicletaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BicicletaService bicicletaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void getBicicletaById_Success() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenReturn(new ResponseEntity<>(bicicleta, org.springframework.http.HttpStatus.OK));

        // Act
        Bicicleta result = bicicletaService.getBicicletaById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }*/

    @Test
    void getBicicletaById_NotFound() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bicicletaService.getBicicletaById(1));
    }

    @Test
    void getBicicletaById_OtherException() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(Bicicleta.class)))
                .thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bicicletaService.getBicicletaById(1));
    }

    /*@Test
    void atualizarStatus_Success() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        doNothing().when(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class));

        // Act
        bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.DISPONIVEL);

        // Assert
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class));
    }*/

    @Test
    void atualizarStatus_Exception() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        doThrow(new RuntimeException()).when(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bicicletaService.atualizarStatus(bicicleta, StatusBicicleta.DISPONIVEL));
    }
}
