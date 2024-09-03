package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdministradoraCCService {

    private static final Logger logger = LoggerFactory.getLogger(AdministradoraCCService.class);


    public boolean enviarParaAdministradoraCC(CartaoDeCredito cartaoDeCredito, BigDecimal valor) {
        try {
            // Criar o mapa de dados para a requisição
            Map<String, Object> cobrancaData = new HashMap<>();
            cobrancaData.put("numeroCartao", cartaoDeCredito.getNumero());
            cobrancaData.put("validade", cartaoDeCredito.getValidade());
            cobrancaData.put("cvv", cartaoDeCredito.getCvv());
            cobrancaData.put("nomeTitular", cartaoDeCredito.getNomeTitular());
            cobrancaData.put("valor", valor);

            // Simulação de verificação
            if (cartaoDeCredito.getNumero() == null || cartaoDeCredito.getValidade() == null ||
                    cartaoDeCredito.getCvv() == null || cartaoDeCredito.getNomeTitular() == null ||
                    valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Dados do cartão de crédito inválidos");
            }

            return true;
        } catch (Exception e) {
            logger.error("Erro ao enviar cobrança para administradora de cartão de crédito", e);
            return false;
        }
    }
}
