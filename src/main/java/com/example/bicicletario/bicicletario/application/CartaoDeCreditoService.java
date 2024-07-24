package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.EmailDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.CartaoDeCreditoRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

@Service
public class CartaoDeCreditoService {

    @Autowired
    private CartaoDeCreditoRepository cartaoDeCreditoRepository;

    @Autowired
    private CiclistaRepository ciclistaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdministradoraCCService administradoraCCService; // Serviço para validação do cartão de crédito

    public CartaoDeCredito obterCartaoDeCredito(int idCiclista) {
        return cartaoDeCreditoRepository.findByCiclistaId(idCiclista).orElseThrow(() -> new ResourceNotFoundException("Cartão de crédito não encontrado."));
    }

    public void alterarCartaoDeCredito(int idCiclista, NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        validarCartaoDeCredito(novoCartaoDeCreditoDTO);

        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoRepository.findByCiclistaId(idCiclista).orElseThrow(() -> new ResourceNotFoundException("Cartão de crédito não encontrado."));
        cartaoDeCredito.setNomeTitular(novoCartaoDeCreditoDTO.getNomeTitular());
        cartaoDeCredito.setNumero(novoCartaoDeCreditoDTO.getNumero());
        cartaoDeCredito.setValidade(novoCartaoDeCreditoDTO.getValidade());
        cartaoDeCredito.setCvv(novoCartaoDeCreditoDTO.getCvv());
        cartaoDeCreditoRepository.save(cartaoDeCredito);

        enviarEmailAlteracaoDeDados(idCiclista);
    }

    public void validarCartaoDeCredito(NovoCartaoDeCreditoDTO cartaoDeCreditoDTO) {
        if (cartaoDeCreditoDTO.getNomeTitular() == null || cartaoDeCreditoDTO.getNomeTitular().isEmpty()) {
            throw new InvalidDataException("Nome do titular do cartão é obrigatório.");
        }
        if (cartaoDeCreditoDTO.getNumero() == null || !cartaoDeCreditoDTO.getNumero().matches("\\d+")) {
            throw new InvalidDataException("Número do cartão de crédito inválido.");
        }
        if (cartaoDeCreditoDTO.getValidade() == null || !isValidDateFormat(cartaoDeCreditoDTO.getValidade())) {
            throw new InvalidDataException("Data de validade do cartão de crédito inválida. Use o formato yyyy-MM-dd.");
        }
        if (cartaoDeCreditoDTO.getCvv() == null || !cartaoDeCreditoDTO.getCvv().matches("\\d{3,4}")) {
            throw new InvalidDataException("CVV do cartão de crédito inválido.");
        }

        validarCartaoJuntoACC(cartaoDeCreditoDTO);
    }

    private boolean isValidDateFormat(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void validarCartaoJuntoACC(NovoCartaoDeCreditoDTO cartaoDeCredito) {
        try {
            administradoraCCService.validarCartao(cartaoDeCredito, true);
        } catch (Exception e) {
            throw new InvalidDataException("Cartão de crédito inválido.");
        }
    }

    private void enviarEmailAlteracaoDeDados(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new ResourceNotFoundException("Ciclista não encontrado."));
        Logger.getLogger("E-mail de confirmação enviado para: " + ciclista.getEmail());
        // Simulação de envio de e-mail
        EmailDTO email = new EmailDTO();
        email.setEmail(ciclista.getEmail());
        email.setAssunto("Alteração de dados");
        email.setMensagem("Seus dados foram alterados com sucesso.");
        emailService.enviarEmail(email);
    }
}
