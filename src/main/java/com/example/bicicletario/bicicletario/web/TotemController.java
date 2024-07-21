package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/totem")
public class TotemController {

    private final TotemService totemService;

    public TotemController(TotemService totemService) {
        this.totemService = totemService;
    }

    @GetMapping
    public ResponseEntity<List<Totem>> listarTotens() {
        List<Totem> totens = totemService.listarTotens();
        return ResponseEntity.ok(totens);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarTotem(@RequestBody NovoTotemDTO totemDTO) {
        try {
            Totem totemCadastrado = totemService.cadastrarTotem(totemDTO);
            return ResponseEntity.status(HttpStatus.OK).body(totemCadastrado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PutMapping("/{idTotem}")
    public ResponseEntity<?> editarTotem(@PathVariable Long idTotem, @RequestBody NovoTotemDTO totemDTO) {
        try {
            Totem totemEditado = totemService.editarTotem(idTotem, totemDTO);
            return ResponseEntity.ok(totemEditado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idTotem}")
    public ResponseEntity<?> removerTotem(@PathVariable Long idTotem) {
        try {
            totemService.removerTotem(idTotem);
            return ResponseEntity.ok("Totem removido");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @GetMapping("/{idTotem}/trancas")
    public ResponseEntity<List<Tranca>> listarTrancas(@PathVariable Long idTotem) {
        List<Tranca> trancas = totemService.listarTrancas(idTotem);
        return ResponseEntity.ok(trancas);
    }

    @GetMapping("/{idTotem}/bicicletas")
    public ResponseEntity<List<Bicicleta>> listarBicicletas(@PathVariable Long idTotem) {
        List<Bicicleta> bicicletas = totemService.listarBicicletas(idTotem);
        return ResponseEntity.ok(bicicletas);
    }
}
