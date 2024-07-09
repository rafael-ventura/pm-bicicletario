package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.dto.TotemDTO;
import com.example.bicicletario.bicicletario.domain.dto.TrancaDTO;
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
    public ResponseEntity<List<TotemDTO>> listarTotens() {
        List<TotemDTO> totens = totemService.listarTotens();
        return ResponseEntity.ok(totens);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarTotem(@RequestBody NovoTotemDTO totemDTO) {
        try {
            TotemDTO totemCadastrado = totemService.cadastrarTotem(totemDTO);
            return ResponseEntity.status(HttpStatus.OK).body(totemCadastrado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PutMapping("/{idTotem}")
    public ResponseEntity<?> editarTotem(@PathVariable Long idTotem, @RequestBody TotemDTO totemDTO) {
        try {
            TotemDTO totemEditado = totemService.editarTotem(idTotem, totemDTO);
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
    public ResponseEntity<List<TrancaDTO>> listarTrancas(@PathVariable Long idTotem) {
        List<TrancaDTO> trancas = totemService.listarTrancas(idTotem);
        return ResponseEntity.ok(trancas);
    }

    @GetMapping("/{idTotem}/bicicletas")
    public ResponseEntity<List<BicicletaDTO>> listarBicicletas(@PathVariable Long idTotem) {
        List<BicicletaDTO> bicicletas = totemService.listarBicicletas(idTotem);
        return ResponseEntity.ok(bicicletas);
    }
}
