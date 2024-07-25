package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidaCartaoDeCreditoService {

    private static final String URL_VALIDACAO = "http://localhost:8080/administradoraCC/validar";



    public boolean validarCartao(NovoCartaoDeCreditoDTO cartaoDeCredito) {
        if (!validarCamposCartao(cartaoDeCredito)) {
            return false;
        }

        try {
            // retornando true para simular validação do cartão na operadora CC
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarCamposCartao(NovoCartaoDeCreditoDTO cartaoDeCredito) {
        if (cartaoDeCredito.getNumero() == null || cartaoDeCredito.getNumero().isEmpty()) {
            return false;
        }
        if (cartaoDeCredito.getValidade() == null || cartaoDeCredito.getValidade().isEmpty()) {
            return false;
        }
        if (cartaoDeCredito.getCvv() == null || cartaoDeCredito.getCvv().isEmpty()) {
            return false;
        }
        if (cartaoDeCredito.getNomeTitular() == null || cartaoDeCredito.getNomeTitular().isEmpty()) {
            return false;
        }
        return true;
    }
}
