package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class AdministradoraCCService {
    // será uma api externa, um outro microserviço, que será, mas nesse momento pode ser apenas um MOCK, com o metodo validarCartao que retorna true ou false

    public void validarCartao(NovoCartaoDeCreditoDTO cartaoDeCredito, boolean value) throws BadRequestException {
        if (value) {
            System.out.println("Cartão válido");
        } else {
            System.out.println("Cartão inválido");
            throw new BadRequestException("Cartão inválido");
        }
    }
}
