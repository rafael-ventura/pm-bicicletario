package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.*;

@RestController
@RequestMapping("/api/tranca")
public class TrancaController {

    private final TrancaService trancaService;

    public TrancaController(TrancaService trancaService) {
        this.trancaService = trancaService;
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<String> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        trancaService.incluirTrancaEmTotem(dto);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<String> retirarDaRede(@RequestBody RetirarTrancaDaRedeDTO dto) {
        trancaService.retirarTrancaDaRede(dto);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @GetMapping
    public ResponseEntity<List<Tranca>> listarTrancas() {
        List<Tranca> trancas = trancaService.listarTodasTrancas();
        return ResponseEntity.ok(trancas);
    }

    @PostMapping
    public ResponseEntity<Tranca> cadastrarTranca(@RequestBody NovaTrancaDTO tranca) {
        Tranca trancaCadastrada = trancaService.cadastrarNovaTranca(tranca);
        return ResponseEntity.ok(trancaCadastrada);
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<Tranca> obterTranca(@PathVariable Long idTranca) {
        Tranca tranca = trancaService.obterTrancaPorId(idTranca);
        return ResponseEntity.ok(tranca);
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<Tranca> editarTranca(@PathVariable Long idTranca, @RequestBody NovaTrancaDTO tranca) {
        Tranca trancaEditada = trancaService.atualizarTranca(idTranca, tranca);
        return ResponseEntity.ok(trancaEditada);
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<String> removerTranca(@PathVariable Long idTranca) {
        trancaService.excluirTranca(idTranca);
        return ResponseEntity.ok(TRANCA_REMOVIDA);
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<Tranca> obterBicicletaNaTranca(@PathVariable Long idTranca) {
        Tranca bicicleta = trancaService.obterBicicletaNaTranca(idTranca);
        return ResponseEntity.ok(bicicleta);
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<String> trancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        trancaService.trancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<String> destrancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        trancaService.destrancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<String> alterarStatusTranca(@PathVariable Long idTranca, @PathVariable String acao) {
        trancaService.alterarStatusTranca(idTranca, acao);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }
}
