package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.ErroDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.exception.EmailAlreadyExistsException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<?> cadastrarCiclista(@RequestBody NovoCiclistaRequestDTO request) {
        try {
            Ciclista ciclista = ciclistaService.cadastrarCiclista(request);
            return ResponseEntity.status(201).body(ciclista);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(422).body(new ErroDTO("422", e.getMessage()));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroDTO("400", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErroDTO("404", e.getMessage()));
        }
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> obterCiclista(@PathVariable Long idCiclista) {
        Optional<Ciclista> ciclista = ciclistaService.obterCiclista(idCiclista);
        return ciclista.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> alterarCiclista(@PathVariable int idCiclista, @RequestBody NovoCiclistaDTO novoCiclistaDTO) {
        Ciclista ciclista = ciclistaService.alterarCiclista(idCiclista, novoCiclistaDTO);
        return ResponseEntity.ok(ciclista);
    }

    @PostMapping("/{idCiclista}/ativar")
    public ResponseEntity<Ciclista> ativarCiclista(@PathVariable Long idCiclista) {
        Ciclista ciclista = ciclistaService.ativarCiclista(idCiclista);
        return ResponseEntity.ok(ciclista);
    }

    @GetMapping("/{idCiclista}/permiteAluguel")
    public ResponseEntity<Boolean> permiteAluguel(@PathVariable int idCiclista) {
        boolean permite = ciclistaService.permiteAluguel(idCiclista);
        return ResponseEntity.ok(permite);
    }

    @GetMapping("/{idCiclista}/bicicletaAlugada")
    public ResponseEntity<Ciclista> obterBicicletaAlugada(@PathVariable Long idCiclista) {
        Optional<Ciclista> bicicleta = ciclistaService.obterBicicletaAlugada(idCiclista);
        return bicicleta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @GetMapping("/existeEmail/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        boolean existe = ciclistaService.existeEmail(email);
        return ResponseEntity.ok(existe);
    }
}
