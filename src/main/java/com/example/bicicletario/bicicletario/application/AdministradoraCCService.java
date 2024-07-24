package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdministradoraCCService {

    private static final Logger logger = LoggerFactory.getLogger(AdministradoraCCService.class);
    private static final String URL_ADMINISTRADORA_CC = "http://localhost:8080/administradoraCC/cobrar";

    @Autowired
    private RestTemplate restTemplate;

    public boolean enviarParaAdministradoraCC(CartaoDeCredito cartaoDeCredito, BigDecimal valor) {
        try {
            // Criar o mapa de dados para a requisição
            Map<String, Object> cobrancaData = new HashMap<>();
            cobrancaData.put("numeroCartao", cartaoDeCredito.getNumero());
            cobrancaData.put("validade", cartaoDeCredito.getValidade());
            cobrancaData.put("cvv", cartaoDeCredito.getCvv());
            cobrancaData.put("nomeTitular", cartaoDeCredito.getNomeTitular());
            cobrancaData.put("valor", valor);

            // Enviar a requisição POST para a administradora de cartão de crédito
            Boolean response = restTemplate.postForObject(URL_ADMINISTRADORA_CC, cobrancaData, Boolean.class);
            return response != null && response;
        } catch (Exception e) {
            logger.error("Erro ao enviar cobrança para administradora de cartão de crédito", e);
            return false;
        }
    }
}
