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
import com.example.bicicletario.bicicletario.domain.mapper.CartaoDeCreditoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class CartaoDeCreditoService {

    private static final Logger logger = LoggerFactory.getLogger(CartaoDeCreditoService.class);

    private final CartaoDeCreditoRepository cartaoDeCreditoRepository;
    private final CiclistaRepository ciclistaRepository;
    private final EmailService emailService;
    private final CartaoDeCreditoMapper cartaoDeCreditoMapper;
    private final AdministradoraCCService administradoraCCService;

    public CartaoDeCreditoService(CartaoDeCreditoRepository cartaoDeCreditoRepository, CiclistaRepository ciclistaRepository, EmailService emailService, CartaoDeCreditoMapper cartaoDeCreditoMapper, AdministradoraCCService administradoraCCService) {
        this.cartaoDeCreditoRepository = cartaoDeCreditoRepository;
        this.ciclistaRepository = ciclistaRepository;
        this.emailService = emailService;
        this.cartaoDeCreditoMapper = cartaoDeCreditoMapper;
        this.administradoraCCService = administradoraCCService;
    }

    public CartaoDeCredito obterCartaoDeCredito(int idCiclista) {
        return cartaoDeCreditoRepository.findByCiclistaId(idCiclista)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão de crédito não encontrado."));
    }

    public void alterarCartaoDeCredito(int idCiclista, NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoRepository.findByCiclistaId(idCiclista)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão de crédito não encontrado."));

        validarCartaoDeCredito(novoCartaoDeCreditoDTO);

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

        administradoraCCService.validarCartao(cartaoDeCreditoDTO);
    }

    private boolean isValidDateFormat(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public void enviarEmailAlteracaoDeDados(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new ResourceNotFoundException("Ciclista não encontrado com o ID:"));
        logger.info("E-mail de confirmação enviado para: {}", ciclista.getEmail());
        EmailDTO email = new EmailDTO();
        email.setEmail(ciclista.getEmail());
        email.setAssunto("Alteração de dados");
        email.setMensagem("Seus dados foram alterados com sucesso.");

        boolean emailEnviado = emailService.enviarEmail(email);
        if (!emailEnviado) {
            logger.error("Falha ao enviar email para {}", ciclista.getEmail());
            throw new RuntimeException("Erro ao enviar e-mail de confirmação.");
        }
    }

    public void save(NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO, int idCiclista) {
        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoMapper.toEntity(novoCartaoDeCreditoDTO);
        cartaoDeCredito.setIdCiclista(idCiclista);
        cartaoDeCreditoRepository.save(cartaoDeCredito);
    }
}
