package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.dto.IntegrarNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarDaRedeDTO;
import com.example.bicicletario.bicicletario.dto.TrancaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tranca")
public class TrancaController {

    private final TrancaService trancaService;

    public TrancaController(TrancaService trancaService) {
        this.trancaService = trancaService;
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<?> integrarNaRede(@RequestBody IntegrarNaRedeDTO dto) {
        try {
            trancaService.integrarNaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Dados cadastrados");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<?> retirarDaRede(@RequestBody RetirarDaRedeDTO dto) {
        try {
            trancaService.retirarDaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Dados cadastrados");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTrancas() {
        return ResponseEntity.ok(trancaService.listarTrancas());
    }

    @PostMapping
    public ResponseEntity<?> cadastrarTranca(@RequestBody TrancaDTO tranca) {
        return ResponseEntity.status(HttpStatus.OK).body(trancaService.cadastrarTranca(tranca));
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<?> obterTranca(@PathVariable Long idTranca) {
        return ResponseEntity.ok(trancaService.obterTranca(idTranca));
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<?> editarTranca(@PathVariable Long idTranca, @RequestBody Tranca tranca) {
        return ResponseEntity.ok(trancaService.editarTranca(idTranca, tranca));
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<?> removerTranca(@PathVariable Long idTranca) {
        trancaService.removerTranca(idTranca);
        return ResponseEntity.ok("Tranca removida");
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<?> obterBicicletaNaTranca(@PathVariable Long idTranca) {
        return ResponseEntity.ok(trancaService.obterBicicletaNaTranca(idTranca));
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<?> trancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        trancaService.trancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok("Ação bem sucedida");
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<?> destrancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        trancaService.destrancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok("Ação bem sucedida");
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<?> alterarStatusTranca(@PathVariable Long idTranca, @PathVariable String acao) {
        trancaService.alterarStatusTranca(idTranca, acao);
        return ResponseEntity.ok("Ação bem sucedida");
    }
}
