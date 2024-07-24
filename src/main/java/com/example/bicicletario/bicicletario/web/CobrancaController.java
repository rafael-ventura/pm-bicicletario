package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    @PostMapping("/cobranca")
    public ResponseEntity<?> realizarCobranca(@RequestBody NovoCobrancaDTO novaCobranca) {
        try {
            Cobranca cobranca = cobrancaService.realizarCobranca(novaCobranca);
            return ResponseEntity.status(200).body("Cobrança solicitada");
        } catch (Exception e) {
            Erro erro = new Erro("422", "Dados Inválidos");
            return ResponseEntity.status(422).body(erro);
        }
    }

    @GetMapping("/{idCobranca}")
    public ResponseEntity<?> obterCobranca(@PathVariable int idCobranca) {
        Cobranca cobranca = cobrancaService.obterCobrancaPorId(idCobranca);
        if (cobranca != null) {
            return ResponseEntity.ok(cobranca);
        } else {
            Erro erro = new Erro("404", "Cobrança não encontrada");
            return ResponseEntity.status(404).body(erro);
        }
    }

}
