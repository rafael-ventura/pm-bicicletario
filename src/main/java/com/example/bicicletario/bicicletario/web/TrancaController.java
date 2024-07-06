package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.dto.TrancaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tranca")
public class TrancaController {

    private final TrancaService trancaService;

    public TrancaController(TrancaService trancaService) {
        this.trancaService = trancaService;
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<String> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        try {
            trancaService.integrarNaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Dados cadastrados");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<String> retirarDaRede(@RequestBody RetirarTrancaDaRedeDTO dto) {
        try {
            trancaService.retirarDaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Tranca foi retirada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Tranca>> listarTrancas() {
        List<Tranca> trancas = trancaService.listarTrancas();
        return ResponseEntity.ok(trancas);
    }

    @PostMapping
    public ResponseEntity<Tranca> cadastrarTranca(@RequestBody TrancaDTO tranca) {
        Tranca trancaCadastrada = trancaService.cadastrarTranca(tranca);
        return ResponseEntity.status(HttpStatus.OK).body(trancaCadastrada);
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<Tranca> obterTranca(@PathVariable Long idTranca) {
        Tranca tranca = trancaService.obterTranca(idTranca);
        return ResponseEntity.ok(tranca);
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<Tranca> editarTranca(@PathVariable Long idTranca, @RequestBody TrancaDTO tranca) {
        Tranca trancaEditada = trancaService.editarTranca(idTranca, tranca);
        return ResponseEntity.ok(trancaEditada);
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<String> removerTranca(@PathVariable Long idTranca) {
        trancaService.removerTranca(idTranca);
        return ResponseEntity.ok("Tranca removida");
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<Tranca> obterBicicletaNaTranca(@PathVariable Long idTranca) {
        Tranca bicicleta = trancaService.obterBicicletaNaTranca(idTranca);
        return ResponseEntity.ok(bicicleta);
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<String> trancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        trancaService.trancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok("Ação bem sucedida");
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<String> destrancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        trancaService.destrancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok("Ação bem sucedida");
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<String> alterarStatusTranca(@PathVariable Long idTranca, @PathVariable String acao) {
        trancaService.alterarStatusTranca(idTranca, acao);
        return ResponseEntity.ok("Ação bem sucedida");
    }
}
