package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.bicicletario.bicicletario.application.utils.ErroUtil.*;
import static com.example.bicicletario.bicicletario.domain.constants.Constantes.*;

@RestController
@RequestMapping("/api/totem")
public class TotemController {

    private final TotemService totemService;

    public TotemController(TotemService totemService) {
        this.totemService = totemService;
    }

    @GetMapping
    public ResponseEntity listarTotens() {
        try {
            List<Totem> totens = totemService.listarTotens();
            return ResponseEntity.ok(totens);
        } catch (Exception e) {
            return erroInterno(ERRO_LISTAR_TOTENS);
        }
    }

    @PostMapping
    public ResponseEntity cadastrarTotem(@RequestBody NovoTotemDTO totemDTO) {
        try {
            Totem totemCadastrado = totemService.cadastrarTotem(totemDTO);
            return ResponseEntity.ok(totemCadastrado);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (Exception e) {
            return erroInterno(ERRO_CRIAR_TOTEM);
        }
    }

    @PutMapping("/{idTotem}")
    public ResponseEntity editarTotem(@PathVariable Long idTotem, @RequestBody NovoTotemDTO totemDTO) {
        try {
            Totem totemEditado = totemService.editarTotem(idTotem, totemDTO);
            return ResponseEntity.ok(totemEditado);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TOTEM_NAO_ENCONTRADO);
        } catch (Exception e) {
            return erroInterno(ERRO_EDITAR_TOTEM);
        }
    }

    @DeleteMapping("/{idTotem}")
    public ResponseEntity removerTotem(@PathVariable Long idTotem) {
        try {
            totemService.removerTotem(idTotem);
            return ResponseEntity.ok(TOTEM_REMOVIDO);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TOTEM_NAO_ENCONTRADO);
        } catch (Exception e) {
            return erroInterno(ERRO_REMOVER_TOTEM);
        }
    }

    @GetMapping("/{idTotem}/trancas")
    public ResponseEntity listarTrancas(@PathVariable Long idTotem) {
        try {
            List<Tranca> trancas = totemService.listarTrancas(idTotem);
            return ResponseEntity.ok(trancas);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TOTEM_NAO_ENCONTRADO);
        } catch (Exception e) {
            return erroInterno(ERRO_LISTAR_TRANCAS);
        }
    }

    @GetMapping("/{idTotem}/bicicletas")
    public ResponseEntity listarBicicletas(@PathVariable Long idTotem) {
        try {
            List<Bicicleta> bicicletas = totemService.listarBicicletas(idTotem);
            return ResponseEntity.ok(bicicletas);
        } catch (IllegalArgumentException e) {
            return erroInvalido(DADOS_INVALIDOS);
        } catch (NoSuchElementException e) {
            return erroNaoEncontrado(TOTEM_NAO_ENCONTRADO);
        } catch (Exception e) {
            return erroInterno(ERRO_LISTAR_BICICLETAS_TOTEM);
        }
    }
}
