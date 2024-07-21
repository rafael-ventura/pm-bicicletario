package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TrancaService;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.bicicletario.bicicletario.application.utils.ErroUtil.*;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.*;

@RestController
@RequestMapping("/api/tranca")
public class TrancaController {

    private final TrancaService trancaService;

    public TrancaController(TrancaService trancaService) {
        this.trancaService = trancaService;
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<?> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        try {
            trancaService.integrarNaRede(dto);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_INTEGRAR_TRANCA);
        }
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<?> retirarDaRede(@RequestBody RetirarTrancaDaRedeDTO dto) {
        try {
            trancaService.retirarDaRede(dto);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_RETIRAR_TRANCA);
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTrancas() {
        try {
            List<Tranca> trancas = trancaService.listarTrancas();
            return ResponseEntity.ok(trancas);
        } catch (Exception e) {
            return erroInterno(ERRO_LISTAR_TRANCAS);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarTranca(@RequestBody NovaTrancaDTO tranca) {
        try {
            Tranca trancaCadastrada = trancaService.cadastrarTranca(tranca);
            return ResponseEntity.ok(trancaCadastrada);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_CRIAR_TRANCA);
        }
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<?> obterTranca(@PathVariable Long idTranca) {
        try {
            Tranca tranca = trancaService.obterTranca(idTranca);
            return ResponseEntity.ok(tranca);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TRANCA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_OBTER_TRANCA);
        }
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<?> editarTranca(@PathVariable Long idTranca, @RequestBody NovaTrancaDTO tranca) {
        try {
            Tranca trancaEditada = trancaService.editarTranca(idTranca, tranca);
            return ResponseEntity.ok(trancaEditada);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TRANCA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_EDITAR_TRANCA);
        }
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<?> removerTranca(@PathVariable Long idTranca) {
        try {
            trancaService.removerTranca(idTranca);
            return ResponseEntity.ok(TRANCA_REMOVIDA);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TRANCA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_REMOVER_TRANCA);
        }
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<?> obterBicicletaNaTranca(@PathVariable Long idTranca) {
        try {
            Tranca bicicleta = trancaService.obterBicicletaNaTranca(idTranca);
            return ResponseEntity.ok(bicicleta);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(BICICLETA_NAO_ENCONTRADA);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_OBTER_BICICLETA_TRANCA);
        }
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<?> trancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        try {
            trancaService.trancarTranca(idTranca, bicicletaId);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_TRANCAR_TRANCA);
        }
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<?> destrancarTranca(@PathVariable Long idTranca, @RequestBody(required = false) Long bicicletaId) {
        try {
            trancaService.destrancarTranca(idTranca, bicicletaId);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_DESTRANCAR_TRANCA);
        }
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<?> alterarStatusTranca(@PathVariable Long idTranca, @PathVariable String acao) {
        try {
            trancaService.alterarStatusTranca(idTranca, acao);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TRANCA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_ALTERAR_STATUS_TRANCA);
        }
    }
}
