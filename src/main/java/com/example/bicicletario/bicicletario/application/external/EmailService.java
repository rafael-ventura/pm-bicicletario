package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${externo.base-url}")
    private String baseUrl;

    public boolean enviarEmail(EmailDTO email) {
        String url = baseUrl + "/enviarEmail";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmailDTO> request = new HttpEntity<>(email, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            } else {
                logger.error("Erro ao enviar email. Status code: {}", response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}", email.getEmail(), e);
            throw new BadRequestException("Erro ao enviar email.");
        }
    }

    public void enviarEmailAluguel(int idCiclista, Aluguel aluguel, Bicicleta bicicleta, Tranca tranca) {
        EmailDTO email = new EmailDTO();
        email.setEmail("ciclista" + idCiclista + "@bicicletario.com");
        email.setAssunto("Aluguel de bicicleta");

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Você alugou a bicicleta ")
                .append(bicicleta.getNumero())
                .append(" (")
                .append(bicicleta.getMarca())
                .append(" ")
                .append(bicicleta.getModelo())
                .append(") com sucesso!\n")
                .append("Data/Hora da Retirada: ")
                .append(aluguel.getHoraInicio())
                .append("\nTotem de Bicicletas (Tranca): ")
                .append(tranca.getId())
                .append("\nValor Cobrado: R$ 10,00");

        email.setMensagem(mensagem.toString());
        enviarEmail(email);
    }

    public void enviarEmailAluguelExistente(int idCiclista, Aluguel aluguel) {
        EmailDTO email = new EmailDTO();
        email.setEmail("ciclista" + idCiclista + "@bicicletario.com");
        email.setAssunto("Aluguel existente");

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Você já possui um aluguel ativo.\n")
                .append("Bicicleta: ")
                .append(aluguel.getBicicleta())
                .append("\nData/Hora da Retirada: ")
                .append(aluguel.getHoraInicio())
                .append("\nTotem de Bicicletas (Tranca): ")
                .append(aluguel.getTrancaInicio());

        email.setMensagem(mensagem.toString());
        enviarEmail(email);
    }

    //email de devolucao
    public void enviarEmailDevolucao(int idCiclista, Aluguel aluguel, Bicicleta bicicleta, Tranca tranca, double valorExtra, String cartaoUsado, String statusPagamento, String dataHoraCobranca) {
        EmailDTO email = new EmailDTO();
        email.setEmail("ciclista" + idCiclista + "@bicicletario.com");
        email.setAssunto("Devolução de bicicleta");

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Você devolveu a bicicleta ")
                .append(bicicleta.getNumero())
                .append(" (")
                .append(bicicleta.getMarca())
                .append(" ")
                .append(bicicleta.getModelo())
                .append(") com sucesso!\n")
                .append("Data/Hora da Devolução: ")
                .append(aluguel.getHoraFim())
                .append("\nTotem de Bicicletas (Tranca): ")
                .append(tranca.getId())
                .append("\nValor Cobrado: R$ ")
                .append(valorExtra > 0 ? valorExtra : "0,00 (sem cobrança extra)")
                .append("\nCartão Usado: ")
                .append(cartaoUsado)
                .append("\nStatus do Pagamento: ")
                .append(statusPagamento);

        if (valorExtra > 0 && dataHoraCobranca != null) {
            mensagem.append("\nData/Hora da Cobrança: ")
                    .append(dataHoraCobranca);
        }

        email.setMensagem(mensagem.toString());
        enviarEmail(email);
    }

    // TODO - Entender se so devemos enviar email de confirmacao de dados UC01. Ver se nao eh o Matera que tem que alterar o Status do Ciclista pra ativo e nos pra AguardandoConfirmacao apos mandar o Email.
    public void enviarEmailConfirmacao(Ciclista ciclista) {
    }
}
