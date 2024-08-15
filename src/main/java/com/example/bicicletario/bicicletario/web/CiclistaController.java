package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.application.Constants;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Ciclista> cadastrarCiclista(@RequestBody NovoCiclistaRequestDTO request) {
        Ciclista ciclista = ciclistaService.cadastrarCiclista(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ciclista); // Status 201 Created
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> obterCiclista(@PathVariable int idCiclista) {
        Ciclista ciclista = ciclistaService.obterCiclista(idCiclista)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO));
        return ResponseEntity.ok().body(ciclista);
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> alterarCiclista(@PathVariable int idCiclista, @RequestBody NovoCiclistaRequestDTO novoCiclistaDTO) {
        Ciclista ciclista = ciclistaService.alterarCiclista(idCiclista, novoCiclistaDTO);
        return ResponseEntity.ok(ciclista);
    }

    @PostMapping("/{idCiclista}/ativar")
    public ResponseEntity<Ciclista> ativarCiclista(@PathVariable int idCiclista) {
        Ciclista ciclista = ciclistaService.ativarCiclista(idCiclista);
        return ResponseEntity.ok(ciclista);
    }

    @GetMapping("/{idCiclista}/permiteAluguel")
    public ResponseEntity<Boolean> permiteAluguel(@PathVariable int idCiclista) {
        boolean permite = ciclistaService.permiteAluguel(idCiclista);
        return ResponseEntity.ok(permite);
    }

    @GetMapping("/{idCiclista}/bicicletaAlugada")
    public ResponseEntity<Bicicleta> obterBicicletaAlugada(@PathVariable int idCiclista) {
        Optional<Bicicleta> bicicleta = ciclistaService.obterBicicletaAlugada(idCiclista);
        return bicicleta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/existeEmail/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        boolean exists = ciclistaService.existeEmail(email);
        return ResponseEntity.ok(exists);
    }
}
