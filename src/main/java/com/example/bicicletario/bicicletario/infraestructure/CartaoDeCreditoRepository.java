package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CartaoDeCreditoRepository {
    private final Map<Integer, CartaoDeCredito> cartoesDeCredito = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // Gerador de ID

    public Optional<CartaoDeCredito> findByCiclistaId(int idCiclista) {
        return cartoesDeCredito.values().stream()
                .filter(cartao -> idCiclista == cartao.getIdCiclista())
                .findFirst();
    }

    public List<CartaoDeCredito> findAll() {
        return new ArrayList<>(cartoesDeCredito.values());
    }

    public CartaoDeCredito save(CartaoDeCredito cartaoDeCredito) {
        if (cartaoDeCredito.getId() == 0) {
            cartaoDeCredito.setId(idGenerator.incrementAndGet()); // Atribui novo ID se não existir
        }
        cartoesDeCredito.put(cartaoDeCredito.getId(), cartaoDeCredito);
        return cartaoDeCredito;
    }

    public void delete(CartaoDeCredito cartaoDeCredito) {
        cartoesDeCredito.remove(cartaoDeCredito.getId());
    }
}
