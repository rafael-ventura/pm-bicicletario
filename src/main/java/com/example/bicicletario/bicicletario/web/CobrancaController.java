package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cobranca")
public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    @PostMapping
    public ResponseEntity<Cobranca> realizarCobranca(@RequestBody NovoCobrancaDTO novaCobranca) {
        try {
            Cobranca cobranca = cobrancaService.realizarCobranca(novaCobranca);
            return ResponseEntity.ok(cobranca);
        } catch (Exception e) {
            return ResponseEntity.status(422).build();
        }
    }
}
