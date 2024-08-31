package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoTrancaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    public void enviarEmail(EmailDTO email) {
        logger.info("Enviando email para {} com a mensagem: {} e o assunto: {}",
                email.getEmail(), email.getMensagem(), email.getAssunto());
    }

    public void enviarEmailAluguel(int idCiclista, Aluguel aluguel, Bicicleta bicicleta, NovoTrancaDTO tranca) {
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

    public void enviarEmailDevolucao(int idCiclista, Devolucao devolucao) {
        EmailDTO email = new EmailDTO();
        email.setEmail("ciclista" + idCiclista + "@bicicletario.com");
        email.setAssunto("Devolução de bicicleta");
        email.setMensagem("Você devolveu a bicicleta " + devolucao.getIdBicicleta() + " com sucesso!");
        enviarEmail(email);
    }
}
