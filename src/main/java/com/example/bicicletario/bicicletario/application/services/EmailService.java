package com.example.bicicletario.bicicletario.application.services;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.dto.EmailDto;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    private final FuncionarioService funcionarioService;
    private final RestTemplate restTemplate;

    public EmailService(FuncionarioService funcionarioService, RestTemplate restTemplate) {
        this.funcionarioService = funcionarioService;
        this.restTemplate = restTemplate;
    }

    public void enviarEmail(String email, String assunto, String mensagem) {
        String baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8060/api";
        String url = baseUrl + "/enviarEmail";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmailDto novoEmail = new EmailDto(email, assunto, mensagem);
        HttpEntity<EmailDto> request = new HttpEntity<>(novoEmail, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new BadRequestException("Erro ao enviar e-mail");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadRequestException("Erro ao enviar e-mail");
            }
            throw e;
        }
    }

    public void enviarEmailParaReparador(Integer idFuncionario, String assunto, String mensagem) {
        Funcionario funcionario = funcionarioService.get(idFuncionario);
        if (funcionario == null) {
            throw new ResourceNotFoundException("Funcionário não encontrado");
        }
        enviarEmail(funcionario.getEmail(), assunto, mensagem);
    }

    public void enviarEmailParaBicicleta(Integer idFuncionario, Bicicleta bicicleta, Tranca tranca, String acao) {
        String assunto = String.format("%s de Bicicleta na Rede", acao);
        String mensagem = String.format(
                """
                        Bicicleta %s da rede:
                        Número: %d
                        Marca: %s
                        Modelo: %s
                        Ano: %s
                        Tranca: %s
                        Data de %s: %s
                        Status após %s: %s
                        Funcionário Responsável: %d""",
                acao.toLowerCase(),
                bicicleta.getNumero(),
                bicicleta.getMarca(),
                bicicleta.getModelo(),
                bicicleta.getAno(),
                tranca.getId(),
                acao.toLowerCase(),
                acao.equals("Inclusão") ? bicicleta.getDataInsercaoTranca() : bicicleta.getDataRemocaoTranca(),
                acao.toLowerCase(),
                bicicleta.getStatusBicicleta().name(),
                idFuncionario
        );
        enviarEmailParaReparador(idFuncionario, assunto, mensagem);
    }

    public void enviarEmailParaTranca(Integer idFuncionario, Tranca tranca, String acao) {
        String assunto = String.format("%s de Tranca no Totem", acao);
        String mensagem = String.format(
                """
                        Tranca %s no totem:
                        Número: %d
                        Modelo: %s
                        Ano de Fabricação: %s
                        Localização: %s
                        Data de %s: %s
                        Status após %s: %s
                        Funcionário Responsável: %d""",
                acao.toLowerCase(),
                tranca.getNumero(),
                tranca.getModelo(),
                tranca.getAnoDeFabricacao(),
                tranca.getLocalizacao(),
                acao.toLowerCase(),
                acao.equals("Inclusão") ? tranca.getDataInsercaoTotem() : tranca.getDataRemocaoTotem(),
                acao.toLowerCase(),
                tranca.getStatus().name(),
                idFuncionario
        );
        enviarEmailParaReparador(idFuncionario, assunto, mensagem);
    }
}
