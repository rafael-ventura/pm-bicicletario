package com.example.bicicletario.bicicletario.application;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.CobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final AdministradoraCCService administradoraCCService;

    @Autowired
    public CobrancaService(CobrancaRepository cobrancaRepository,
                           AdministradoraCCService administradoraCCService) {
        this.cobrancaRepository = cobrancaRepository;
        this.administradoraCCService = administradoraCCService;
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

    // Método para recuperar dados do cartão de crédito
    private CartaoDeCredito recuperarCartaoDeCredito(int idCiclista) {
        // simulando ida a /cartaoDeCredito/{idCiclista}
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(idCiclista);
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");
        cartaoDeCredito.setNumero("5269 2079 9840 6777");
        cartaoDeCredito.setValidade("23/05/2025");
        return cartaoDeCredito;
    }

}
