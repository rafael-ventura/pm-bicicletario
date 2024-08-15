package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdministradoraCCService {
    // será uma api externa, um outro microserviço, que será, mas nesse momento pode ser apenas um MOCK, com o metodo validarCartao que retorna true ou false
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(AdministradoraCCService.class);
    public boolean validarCartao(NovoCartaoDeCreditoDTO cartaoDeCredito, boolean value) throws BadRequestException {
        if (value && cartaoDeCredito.getNumero() != null) {
            logger.info("Cartão válido");
            return true;
        } else {
            logger.error("Cartão inválido");
            throw new InvalidDataException("Cartão inválido");
        }
    }

    public boolean processarPagamento() {
        return true;
    }
}
