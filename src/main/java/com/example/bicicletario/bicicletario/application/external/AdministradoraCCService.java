package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class AdministradoraCCService {
    // será uma api externa, um outro microserviço, que será, mas nesse momento pode ser apenas um MOCK, com o metodo validarCartao que retorna true ou false

    public boolean validarCartao(NovoCartaoDeCreditoDTO cartaoDeCredito, boolean value) throws BadRequestException {
        if (value && cartaoDeCredito.getNumero() != null) {
            // change sysout to logger
            Logger.getLogger("AdministradoraCCService").info("Cartão válido");
            return true;
        } else {
            Logger.getLogger("AdministradoraCCService").severe("Cartão inválido");
            throw new InvalidDataException("Cartão inválido");
        }
    }
}
