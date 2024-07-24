package com.example.bicicletario.bicicletario.application;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidaCartaoDeCreditoService {

    private static final String URL_VALIDACAO = "http://localhost:8080/administradoraCC/validar";

    public boolean validarCartao(NovoCartaoDeCreditoDTO cartaoDeCredito) {

//        RestTemplate restTemplate = new RestTemplate();

        try {
            // Enviar a requisição POST para a administradora de cartão de crédito
            /*
            Boolean response = restTemplate.postForObject(URL_VALIDACAO, cartaoDeCredito, Boolean.class);
            return response != null && response;
            */
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
