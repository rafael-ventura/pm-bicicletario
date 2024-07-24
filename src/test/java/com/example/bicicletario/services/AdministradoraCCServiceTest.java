package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdministradoraCCServiceTest {

    @InjectMocks
    private AdministradoraCCService administradoraCCService;

    @BeforeEach
    void setUp() {
        administradoraCCService = new AdministradoraCCService();
    }

    @Test
    void validarCartao_validCartao_noExceptionThrown() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        // set the necessary fields in cartaoDeCredito

        assertDoesNotThrow(() -> administradoraCCService.validarCartao(cartaoDeCredito, true));
    }

    @Test
    void validarCartao_invalidCartao_throwBadRequestException() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        // set the necessary fields in cartaoDeCredito

        BadRequestException exception = assertThrows(BadRequestException.class, () -> administradoraCCService.validarCartao(cartaoDeCredito, false));
        assertEquals("Cartão inválido", exception.getMessage());
    }
}
