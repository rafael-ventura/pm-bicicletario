package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CartaoDeCreditoService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.ErroDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
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
    public ResponseEntity<?> obterCartaoDeCredito(@PathVariable Long idCiclista) {
        try {
            CartaoDeCredito cartaoDeCredito = cartaoDeCreditoService.obterCartaoDeCredito(idCiclista);
            return ResponseEntity.ok(cartaoDeCredito);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(422).body(new ErroDTO("422", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErroDTO("404", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErroDTO("500", "Erro inesperado."));
        }
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<?> alterarCartaoDeCredito(@PathVariable Long idCiclista, @RequestBody NovoCartaoDeCreditoDTO novoCartaoDeCreditoDTO) {
        try {
            cartaoDeCreditoService.alterarCartaoDeCredito(idCiclista, novoCartaoDeCreditoDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(422).body(new ErroDTO("422", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErroDTO("404", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErroDTO("500", "Erro inesperado."));
        }
    }
}
