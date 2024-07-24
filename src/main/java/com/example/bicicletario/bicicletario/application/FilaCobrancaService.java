package com.example.bicicletario.bicicletario.application;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.FilaCobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
public class FilaCobrancaService {

    @Autowired
    private FilaCobrancaRepository filaCobrancaRepository;

    public Cobranca adicionarNaFila(NovoCobrancaDTO novaCobranca) {
        // Validação do valor
        if (novaCobranca.getValor() == null || novaCobranca.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }

        // Criação do objeto Cobranca
        Cobranca cobranca = new Cobranca();
        cobranca.setCiclista(novaCobranca.getCiclista());
        cobranca.setValor(novaCobranca.getValor());
        cobranca.setStatusCobranca(StatusCobranca.PENDENTE);
        cobranca.setHoraSolicitacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        // Adicionar a cobrança na fila
        filaCobrancaRepository.adicionarNaFila(cobranca);

        return cobranca;
    }

    public List<Cobranca> processarFila() {
        List<Cobranca> cobrancasProcessadas = new ArrayList<>();

        while (!filaCobrancaRepository.isEmpty()) {
            Cobranca cobranca = filaCobrancaRepository.removerDaFila();
            // Simular envio para administradora de cartão de crédito
            boolean pagamentoConfirmado = enviarParaAdministradoraCC(cobranca);

            if (pagamentoConfirmado) {
                cobranca.setStatusCobranca(StatusCobranca.PAGA);
                cobranca.setHoraFinalizacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            } else {
                cobranca.setStatusCobranca(StatusCobranca.FALHA);
            }

            cobrancasProcessadas.add(cobranca);
        }

        return cobrancasProcessadas;
    }

    private boolean enviarParaAdministradoraCC(Cobranca cobranca) {
        // Lógica de integração com administradora de cartão de crédito
        return true; // Simulação de sucesso
    }
}
