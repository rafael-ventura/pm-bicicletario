package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class ValidaCartaoDeCreditoService {

    private static final Logger LOGGER = Logger.getLogger(ValidaCartaoDeCreditoService.class.getName());

    public boolean validarCartao(NovoCartaoDeCreditoDTO cartaoDeCredito) {
        if (!validarCamposCartao(cartaoDeCredito)) {
            LOGGER.warning("Campos do cartão inválidos.");
            return false;
        }

        if (!validarNumeroCartao(cartaoDeCredito.getNumero())) {
            LOGGER.warning("Número do cartão inválido.");
            return false;
        }

        if (!validarValidadeCartao(cartaoDeCredito.getValidade())) {
            LOGGER.warning("Data de validade do cartão inválida.");
            return false;
        }

        if (!validarCvv(cartaoDeCredito.getCvv())) {
            LOGGER.warning("CVV do cartão inválido.");
            return false;
        }

        LOGGER.info("Cartão validado com sucesso.");
        return true;
    }

    private boolean validarCamposCartao(NovoCartaoDeCreditoDTO cartaoDeCredito) {
        return cartaoDeCredito.getNumero() != null && !cartaoDeCredito.getNumero().isEmpty()
                && cartaoDeCredito.getValidade() != null && !cartaoDeCredito.getValidade().isEmpty()
                && cartaoDeCredito.getCvv() != null && !cartaoDeCredito.getCvv().isEmpty()
                && cartaoDeCredito.getNomeTitular() != null && !cartaoDeCredito.getNomeTitular().trim().isEmpty();
    }

    private boolean validarNumeroCartao(String numero) {
        if (numero == null || !numero.matches("\\d{13,19}")) {
            return false;
        }
        return validarLuhn(numero);
    }

    private boolean validarLuhn(String numero) {
        int soma = 0;
        boolean alternar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numero.substring(i, i + 1));
            if (alternar) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            soma += n;
            alternar = !alternar;
        }
        return (soma % 10 == 0);
    }

    private boolean validarValidadeCartao(String validade) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth dataValidade = YearMonth.parse(validade, formatter);
            return dataValidade.isAfter(YearMonth.now());
        } catch (DateTimeParseException e) {
            LOGGER.warning("Formato de data inválido: " + validade);
            return false;
        }
    }

    private boolean validarCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }
}
