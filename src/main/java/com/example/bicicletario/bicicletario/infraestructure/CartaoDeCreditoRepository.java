package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CartaoDeCreditoRepository {
    private final Map<Integer, CartaoDeCredito> cartoesDeCredito = new HashMap<>();

    public Optional<CartaoDeCredito> findByCiclistaId(int idCiclista) {
        return cartoesDeCredito.values().stream()
                .filter(cartao -> idCiclista == cartao.getIdCiclista())
                .findFirst();
    }

    public List<CartaoDeCredito> findAll() {
        return new ArrayList<>(cartoesDeCredito.values());
    }

    public CartaoDeCredito save(CartaoDeCredito cartaoDeCredito) {
        cartoesDeCredito.put(cartaoDeCredito.getId(), cartaoDeCredito);
        return cartaoDeCredito;
    }

    public void delete(CartaoDeCredito cartaoDeCredito) {
        cartoesDeCredito.remove(cartaoDeCredito.getId());
    }
}