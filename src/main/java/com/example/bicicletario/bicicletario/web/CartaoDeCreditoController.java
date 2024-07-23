package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartaoDeCredito")
public class CartaoDeCreditoController {

    private final CartaoDeCreditoService cartaoDeCreditoService;

    public CartaoDeCreditoController(CartaoDeCreditoService cartaoDeCreditoService) {
        this.cartaoDeCreditoService = cartaoDeCreditoService;
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<CartaoDeCredito> obterCartaoDeCredito(@PathVariable int idCiclista) {
        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoService.obterCartaoDeCredito(idCiclista);
        return ResponseEntity.ok(cartaoDeCredito);
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<NovoCartaoDeCreditoDTO> alterarCartaoDeCredito(@PathVariable int idCiclista, @RequestBody NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        cartaoDeCreditoService.alterarCartaoDeCredito(idCiclista, novoCartaoDeCreditoDTO);
        return ResponseEntity.ok().build();
    }
}
