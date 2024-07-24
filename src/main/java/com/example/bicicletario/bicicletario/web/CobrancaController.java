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
@RequestMapping("/api/cobranca")
public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    @PostMapping
    public ResponseEntity<?> realizarCobranca(@RequestBody NovoCobrancaDTO novaCobranca) {
        try {
            System.out.println("Realizando cobranca");
            Cobranca cobranca = cobrancaService.realizarCobranca(novaCobranca);
            System.out.println("cobranca realizada com sucesso");
            return ResponseEntity.status(200).body(cobranca);
        } catch (Exception e) {
            System.out.println("Erro na cobranca");
            Erro erro = new Erro("422", "Dados Inválidos");
            return ResponseEntity.status(422).body(erro);
        }
    }

    @GetMapping("/{idCobranca}")
    public ResponseEntity<?> obterCobranca(@PathVariable int idCobranca) {
        System.out.println("Pegando cobranca");
        Cobranca cobranca = cobrancaService.obterCobrancaPorId(idCobranca);
        if (cobranca != null) {
            System.out.println("Cobranca realizada com sucesso");
            return ResponseEntity.ok(cobranca);
        } else {
            Erro erro = new Erro("404", "Cobrança não encontrada");
            System.out.println(erro);
            return ResponseEntity.status(404).body(erro);
        }
    }

}
