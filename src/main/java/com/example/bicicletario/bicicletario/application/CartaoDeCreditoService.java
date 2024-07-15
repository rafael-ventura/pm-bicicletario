package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.CartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
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

    public Optional<CartaoDeCreditoDTO> obterCartaoDeCredito(Long idCiclista) {
        return cartaoDeCreditoRepository.findByCiclistaId(idCiclista)
                .map(cartaoDeCreditoMapper::toDto);
    }

    public CartaoDeCreditoDTO alterarCartaoDeCredito(Long idCiclista, NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        if (!validarCartaoDeCredito(novoCartaoDeCreditoDTO)) {
            throw new IllegalArgumentException("Cartão de crédito inválido.");
        }

        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoMapper.toEntity(novoCartaoDeCreditoDTO);
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        cartaoDeCredito.setCiclista(ciclista);
        cartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);
        return cartaoDeCreditoMapper.toDto(cartaoDeCredito);
    }

    private boolean validarCartaoDeCredito(NovoCartaoDeCreditoDTO cartaoDeCreditoDTO) {
        // Simulação de validação de cartão de crédito
        return true;
    }
}
