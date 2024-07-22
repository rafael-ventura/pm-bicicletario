package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.apache.coyote.BadRequestException;

public class AdministradoraCCService {
    // será uma api externa, um outro microserviço, que será, mas nesse momento pode ser apenas um MOCK, com o metodo validarCartao que retorna true ou false

    public void validarCartao(CartaoDeCredito cartaoDeCredito, boolean value) throws BadRequestException {
        if (value) {
            System.out.println("Cartão válido");
        } else {
            System.out.println("Cartão inválido");
            throw new BadRequestException("Cartão inválido");
        }
    }
}
