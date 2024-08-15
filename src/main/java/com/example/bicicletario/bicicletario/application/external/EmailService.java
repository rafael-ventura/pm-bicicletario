package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);
    public void enviarEmail(EmailDTO email) {
        logger.info("Enviando email para {} com a mensagem: {} e o assunto: {}",
                email.getEmail(), email.getMensagem(), email.getAssunto());
    }

    public void enviarEmailAluguel(int idCiclista, Aluguel aluguel) {
        EmailDTO email = new EmailDTO();
        email.setEmail("ciclista" + idCiclista + "@bicicletario.com");
        email.setAssunto("Aluguel de bicicleta");
        email.setMensagem("Você alugou a bicicleta " + aluguel.getBicicleta() + " com sucesso!");
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
