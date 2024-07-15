package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.CiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ciclista")
public class CiclistaController {

    private final CiclistaService ciclistaService;

    public CiclistaController(CiclistaService ciclistaService) {
        this.ciclistaService = ciclistaService;
    }

    @PostMapping
    public ResponseEntity<CiclistaDTO> cadastrarCiclista(@RequestBody NovoCiclistaDTO novoCiclistaDTO) {
        CiclistaDTO ciclista = ciclistaService.cadastrarCiclista(novoCiclistaDTO);
        return ResponseEntity.status(201).body(ciclista);
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<CiclistaDTO> obterCiclista(@PathVariable Long idCiclista) {
        Optional<CiclistaDTO> ciclista = ciclistaService.obterCiclista(idCiclista);
        return ciclista.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<CiclistaDTO> alterarCiclista(@PathVariable Long idCiclista, @RequestBody NovoCiclistaDTO novoCiclistaDTO) {
        CiclistaDTO ciclista = ciclistaService.alterarCiclista(idCiclista, novoCiclistaDTO);
        return ResponseEntity.ok(ciclista);
    }

    @PostMapping("/{idCiclista}/ativar")
    public ResponseEntity<CiclistaDTO> ativarCiclista(@PathVariable Long idCiclista) {
        CiclistaDTO ciclista = ciclistaService.ativarCiclista(idCiclista);
        return ResponseEntity.ok(ciclista);
    }

    @GetMapping("/{idCiclista}/permiteAluguel")
    public ResponseEntity<Boolean> permiteAluguel(@PathVariable Long idCiclista) {
        boolean permite = ciclistaService.permiteAluguel(idCiclista);
        return ResponseEntity.ok(permite);
    }

    @GetMapping("/{idCiclista}/bicicletaAlugada")
    public ResponseEntity<CiclistaDTO> obterBicicletaAlugada(@PathVariable Long idCiclista) {
        Optional<CiclistaDTO> bicicleta = ciclistaService.obterBicicletaAlugada(idCiclista);
        return bicicleta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @GetMapping("/existeEmail/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        boolean existe = ciclistaService.existeEmail(email);
        return ResponseEntity.ok(existe);
    }
}
