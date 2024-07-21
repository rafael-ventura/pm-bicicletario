package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static com.example.bicicletario.bicicletario.application.utils.ErroUtil.*;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.*;

@RestController
@RequestMapping("/api/bicicleta")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping
    public ResponseEntity<?> listarBicicletas() {
        try {
            return ResponseEntity.ok(bicicletaService.listarBicicletas());
        } catch (Exception e) {
            return erroInterno(ERRO_LISTAR_BICICLETAS);
        }
    }

    @PostMapping
    public ResponseEntity<?> criarBicicleta(@RequestBody NovaBicicletaDTO bicicleta) {
        try {
            return ResponseEntity.ok(bicicletaService.criarBicicleta(bicicleta));
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_CRIAR_BICICLETA);
        }
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<?> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        try {
            bicicletaService.integrarNaRede(dto);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(STATUS_DA_BICICLETA_INVALIDO);
        } catch (Exception e) {
            return erroInterno(ERRO_INTEGRAR_BICICLETA);
        }
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<?> retirarDaRede(@RequestBody RetirarBicicletaDaRedeDTO dto) {
        try {
            bicicletaService.retirarDaRede(dto);
            return ResponseEntity.ok(DADOS_CADASTRADOS);
        } catch (IllegalArgumentException e) {
            return erroInvalido(STATUS_DA_BICICLETA_INVALIDO);
        } catch (Exception e) {
            return erroInterno(ERRO_RETIRAR_BICICLETA);
        }
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<?> obterBicicleta(@PathVariable Long idBicicleta) {
        try {
            return ResponseEntity.ok(bicicletaService.obterBicicleta(idBicicleta));
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(BICICLETA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_OBTER_BICICLETA);
        }
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<?> editarBicicleta(@PathVariable Long idBicicleta, @RequestBody NovaBicicletaDTO bicicletaDTO) {
        try {
            return ResponseEntity.ok(bicicletaService.editarBicicleta(idBicicleta, bicicletaDTO));
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(BICICLETA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_EDITAR_BICICLETA);
        }
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<?> removerBicicleta(@PathVariable Long idBicicleta) {
        try {
            bicicletaService.removerBicicleta(idBicicleta);
            return ResponseEntity.ok(BICICLETA_REMOVIDA);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(BICICLETA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_REMOVER_BICICLETA);
        }
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<?> alterarStatusBicicleta(@PathVariable Long idBicicleta, @PathVariable String acao) {
        try {
            return ResponseEntity.ok(bicicletaService.alterarStatusBicicleta(idBicicleta, acao));
        } catch (IllegalArgumentException e) {
            return erroInvalido(STATUS_DA_BICICLETA_INVALIDO);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(BICICLETA_NAO_ENCONTRADA);
        } catch (Exception e) {
            return erroInterno(ERRO_ALTERAR_STATUS_BICICLETA);
        }
    }
}
