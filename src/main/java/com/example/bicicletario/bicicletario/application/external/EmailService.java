package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    private static final String TOTEM_BICICLETAS_TRANCA_LABEL = "Totem de Bicicletas (Tranca): ";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean enviarEmail(EmailDTO email) {
        String baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8060/api";
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

    public void enviarEmailAluguel(String ciclistaEmail, Aluguel aluguel, Bicicleta bicicleta, Tranca tranca) {
        EmailDTO email = new EmailDTO();
        email.setEmail(ciclistaEmail);
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
                .append("\n")
                .append(TOTEM_BICICLETAS_TRANCA_LABEL)
                .append(tranca.getId())
                .append("\nValor Cobrado: R$ 10,00");

        email.setMensagem(mensagem.toString());
        enviarEmail(email);
    }

    public void enviarEmailAluguelExistente(String emailCiclista, Aluguel aluguel) {
        EmailDTO email = new EmailDTO();
        email.setEmail(emailCiclista);
        email.setAssunto("Aluguel existente");

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Você já possui um aluguel ativo.\n")
                .append("Bicicleta: ")
                .append(aluguel.getBicicleta())
                .append("\nData/Hora da Retirada: ")
                .append(aluguel.getHoraInicio())
                .append("\n")
                .append(TOTEM_BICICLETAS_TRANCA_LABEL)
                .append(aluguel.getTrancaInicio());

        email.setMensagem(mensagem.toString());
        enviarEmail(email);
    }

    // Email de devolução
    public void enviarEmailDevolucao(String emailCiclista, Aluguel aluguel, Bicicleta bicicleta, Tranca tranca, double valorExtra, String cartaoUsado, String statusPagamento, String dataHoraCobranca) {
        EmailDTO email = new EmailDTO();
        email.setEmail(emailCiclista);
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
                .append("\n")
                .append(TOTEM_BICICLETAS_TRANCA_LABEL)
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

    public void enviarEmailConfirmacao(Ciclista ciclista) {
        // Criação do objeto EmailDTO para enviar o email
        EmailDTO email = new EmailDTO();

        // Configura o email do ciclista para quem será enviado
        email.setEmail(ciclista.getEmail());

        // Define o assunto do email
        email.setAssunto("Confirmação de Cadastro no Bicicletário");

        // Construção da mensagem do email de confirmação
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Olá ").append(ciclista.getNome()).append(",\n\n");
        mensagem.append("Seu cadastro no sistema Bicicletário foi realizado com sucesso!\n");
        mensagem.append("Por favor, clique no link abaixo para confirmar seu cadastro e ativar sua conta:\n");
        mensagem.append("https://bicicletario.com/confirmacao/").append(ciclista.getId()).append("\n\n");
        mensagem.append("Caso você não tenha solicitado esse cadastro, por favor, ignore este email.\n\n");
        mensagem.append("Atenciosamente,\nEquipe Bicicletário");

        // Define a mensagem no objeto EmailDTO
        email.setMensagem(mensagem.toString());

        // Envia o email utilizando o serviço de email
        enviarEmail(email);
    }
}

