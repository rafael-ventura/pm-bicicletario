package com.example.bicicletario.Unitarios.Services;

import com.example.bicicletario.bicicletario.application.ValidaCartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCartaoDeCreditoServiceTest {

    private ValidaCartaoDeCreditoService validaCartaoDeCreditoService;

    @BeforeEach
    void setUp() {
        validaCartaoDeCreditoService = new ValidaCartaoDeCreditoService();
    }

    @Test
    void validarCartaoComSucesso() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("5269 2079 9840 6777");
        cartaoDeCredito.setValidade("23/05/2025");
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");

        boolean resultado = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);
        assertTrue(resultado);
    }

    @Test
    void validarCartaoComNumeroInvalido() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero(null);
        cartaoDeCredito.setValidade("23/05/2025");
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");

        boolean resultado = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);
        assertFalse(resultado);
    }

    @Test
    void validarCartaoComValidadeInvalida() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("5269 2079 9840 6777");
        cartaoDeCredito.setValidade(null);
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");

        boolean resultado = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);
        assertFalse(resultado);
    }

    @Test
    void validarCartaoComCvvInvalido() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("5269 2079 9840 6777");
        cartaoDeCredito.setValidade("23/05/2025");
        cartaoDeCredito.setCvv(null);
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");

        boolean resultado = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);
        assertFalse(resultado);
    }

    @Test
    void validarCartaoComNomeTitularInvalido() {
        NovoCartaoDeCreditoDTO cartaoDeCredito = new NovoCartaoDeCreditoDTO();
        cartaoDeCredito.setNumero("5269 2079 9840 6777");
        cartaoDeCredito.setValidade("23/05/2025");
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular(null);

        boolean resultado = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);
        assertFalse(resultado);
    }
}
