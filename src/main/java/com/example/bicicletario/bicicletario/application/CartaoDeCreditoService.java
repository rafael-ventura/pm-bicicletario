package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.CartaoDeCreditoRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import com.example.bicicletario.bicicletario.mapper.CartaoDeCreditoMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartaoDeCreditoService {

    private final CartaoDeCreditoRepository cartaoDeCreditoRepository;
    private final CartaoDeCreditoMapper cartaoDeCreditoMapper;
    private final CiclistaRepository ciclistaRepository;

    public CartaoDeCreditoService(CartaoDeCreditoRepository cartaoDeCreditoRepository, CartaoDeCreditoMapper cartaoDeCreditoMapper, CiclistaRepository ciclistaRepository) {
        this.cartaoDeCreditoRepository = cartaoDeCreditoRepository;
        this.cartaoDeCreditoMapper = cartaoDeCreditoMapper;
        this.ciclistaRepository = ciclistaRepository;
    }

    public CartaoDeCredito obterCartaoDeCredito(Long idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new ResourceNotFoundException("Ciclista não encontrado."));
        return cartaoDeCreditoRepository.findByNomeTitular(ciclista.getNome()).orElseThrow(() -> new ResourceNotFoundException("Cartão de crédito não encontrado."));
    }

    public void alterarCartaoDeCredito(Long idCiclista, NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        if (!validarCartaoDeCredito(novoCartaoDeCreditoDTO)) {
            throw new IllegalArgumentException("Cartão de crédito inválido.");
        }

        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new IllegalArgumentException("Ciclista não encontrado."));
        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoRepository.findByCiclistaId(idCiclista).orElse(new CartaoDeCredito());

        cartaoDeCredito.setNomeTitular(novoCartaoDeCreditoDTO.getNomeTitular());
        cartaoDeCredito.setNumero(novoCartaoDeCreditoDTO.getNumero());
        cartaoDeCredito.setValidade(novoCartaoDeCreditoDTO.getValidade());
        cartaoDeCredito.setCvv(novoCartaoDeCreditoDTO.getCvv());

        cartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        enviarEmailConfirmacao(ciclista.getEmail());
    }

    private boolean validarCartaoDeCredito(NovoCartaoDeCreditoDTO cartaoDeCreditoDTO) {
        // Simulação de validação de cartão de crédito
        return true;
    }

    private void enviarEmailConfirmacao(String email) {
        System.out.println("E-mail de confirmação enviado para: " + email);
        // Simulação de envio de e-mail
    }
}
