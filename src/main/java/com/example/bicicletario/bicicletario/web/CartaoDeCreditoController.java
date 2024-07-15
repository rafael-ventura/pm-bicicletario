package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.dto.CartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cartaoDeCredito")
public class CartaoDeCreditoController {

    private final CartaoDeCreditoService cartaoDeCreditoService;

    public CartaoDeCreditoController(CartaoDeCreditoService cartaoDeCreditoService) {
        this.cartaoDeCreditoService = cartaoDeCreditoService;
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<CartaoDeCreditoDTO> obterCartaoDeCredito(@PathVariable Long idCiclista) {
        Optional<CartaoDeCreditoDTO> cartaoDeCredito = cartaoDeCreditoService.obterCartaoDeCredito(idCiclista);
        return cartaoDeCredito.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<CartaoDeCreditoDTO> alterarCartaoDeCredito(@PathVariable Long idCiclista, @RequestBody NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        CartaoDeCreditoDTO cartaoDeCredito = cartaoDeCreditoService.alterarCartaoDeCredito(idCiclista, novoCartaoDeCreditoDTO);
        return ResponseEntity.ok(cartaoDeCredito);
    }
}
