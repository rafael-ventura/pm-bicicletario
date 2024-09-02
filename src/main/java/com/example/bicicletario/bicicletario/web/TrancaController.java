package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.services.TrancaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
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
        trancaService.incluirTrancaNaRede(dto);
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
        return ResponseEntity.ok()
                .header("Message", "OK")
                .body(trancas);
    }

    @PostMapping
    public ResponseEntity<Tranca> cadastrarTranca(@RequestBody NovaTrancaDTO tranca) {
        Tranca trancaCadastrada = trancaService.cadastrarNovaTranca(tranca);
        return ResponseEntity.ok(trancaCadastrada);
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<Tranca> obterTranca(@PathVariable Integer idTranca) {
        Tranca tranca = trancaService.obterTrancaPorId(idTranca);
        return ResponseEntity.ok()
                .header("Message", TRANCA_ENCONTRADA)
                .body(tranca);
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<Tranca> editarTranca(@PathVariable Integer idTranca, @RequestBody NovaTrancaDTO tranca) {
        Tranca trancaEditada = trancaService.atualizarTranca(idTranca, tranca);
        return ResponseEntity.ok()
                .header("Message", DADOS_CADASTRADOS)
                .body(trancaEditada);
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<String> removerTranca(@PathVariable Integer idTranca) {
        trancaService.excluirTranca(idTranca);
        return ResponseEntity.ok(TRANCA_REMOVIDA);
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<Bicicleta> obterBicicletaNaTranca(@PathVariable Integer idTranca) {
        Bicicleta bicicleta = trancaService.obterBicicletaNaTranca(idTranca);
        return ResponseEntity.ok()
                .header("Message", TRANCA_ENCONTRADA)
                .body(bicicleta);
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<Tranca> trancarTranca(@PathVariable Integer idTranca, @RequestBody(required = false) Integer bicicletaId) {
        Tranca tranca = trancaService.trancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok()
                .header("Message", ACAO_BEM_SUCEDIDA)
                .body(tranca);
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<Tranca> destrancarTranca(@PathVariable Integer idTranca, @RequestBody(required = false) Integer bicicletaId) {
        Tranca tranca = trancaService.destrancarTranca(idTranca, bicicletaId);
        return ResponseEntity.ok()
                .header("Message", ACAO_BEM_SUCEDIDA)
                .body(tranca);
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<Tranca> alterarStatusTranca(@PathVariable Integer idTranca, @PathVariable String acao) {
        Tranca tranca = trancaService.alterarStatusTranca(idTranca, acao);
        return ResponseEntity.ok()
                .header("Message", ACAO_BEM_SUCEDIDA)
                .body(tranca);
    }
}
