package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.CobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final AdministradoraCCService administradoraCCService;
    private final RestTemplate restTemplate;

    @Autowired
    public CobrancaService(CobrancaRepository cobrancaRepository,
                           AdministradoraCCService administradoraCCService,
                           RestTemplate restTemplate) {
        this.cobrancaRepository = cobrancaRepository;
        this.administradoraCCService = administradoraCCService;
        this.restTemplate = restTemplate;
    }

    public Cobranca obterCobrancaPorId(int idCobranca) {
        return cobrancaRepository.findById(idCobranca);
    }

    public Cobranca realizarCobranca(NovoCobrancaDTO novaCobranca) {
        // Validação do valor
        if (novaCobranca.getValor() == null || novaCobranca.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }

        // Recuperar dados do cartão de crédito
        CartaoDeCredito cartaoDeCredito = recuperarCartaoDeCredito(novaCobranca.getCiclista());

        // Criar cobrança
        Cobranca cobranca = new Cobranca();
        cobranca.setCiclista(novaCobranca.getCiclista());
        cobranca.setValor(novaCobranca.getValor());
        cobranca.setStatusCobranca(StatusCobranca.PENDENTE);
        cobranca.setHoraSolicitacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        // Simular envio para administradora de cartão de crédito
        boolean pagamentoConfirmado = administradoraCCService.enviarParaAdministradoraCC(cartaoDeCredito, novaCobranca.getValor());

        if (pagamentoConfirmado) {
            cobranca.setStatusCobranca(StatusCobranca.PAGA);
            cobranca.setHoraFinalizacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            cobranca.setStatusCobranca(StatusCobranca.FALHA);
        }

        // Salvar cobrança
        cobranca = cobrancaRepository.save(cobranca);

        return cobranca;
    }

    // Método atualizado para recuperar dados do cartão de crédito via requisição HTTP
    private CartaoDeCredito recuperarCartaoDeCredito(int idCiclista) {
        String url = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8040/api/cartaoDeCredito/" + idCiclista;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String body = "{"
                + "\"id\": 0,"
                + "\"nomeTitular\": \"string\","
                + "\"numero\": \"01342274200776582552520486085540897593420429028986327\","
                + "\"validade\": \"2024-09-02\","
                + "\"cvv\": \"966\""
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<CartaoDeCredito> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CartaoDeCredito.class
        );

        return response.getBody();
    }
}
