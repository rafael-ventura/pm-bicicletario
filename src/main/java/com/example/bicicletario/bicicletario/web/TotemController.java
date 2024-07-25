package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.TotemService;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.*;

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
    public ResponseEntity<Totem> criarTotem(@RequestBody NovoTotemDTO totem) {
        Totem totemCadastrado = totemService.cadastrarTotem(totem);
        return ResponseEntity.ok(totemCadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Totem> editarTotem(@PathVariable Long id, @RequestBody NovoTotemDTO totemDTO) {
        Totem totemEditado = totemService.editarTotem(id, totemDTO);
        return ResponseEntity.ok(totemEditado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerTotem(@PathVariable Long id) {
        totemService.removerTotem(id);
        return ResponseEntity.ok(TOTEM_REMOVIDO);
    }

    @GetMapping("/{id}/trancas")
    public ResponseEntity<List<Tranca>> listarTrancas(@PathVariable Long id) {
        List<Tranca> trancas = totemService.listarTrancas(id);
        return ResponseEntity.ok(trancas);
    }

    @GetMapping("/{id}/bicicletas")
    public ResponseEntity<List<Bicicleta>> listarBicicletas(@PathVariable Long id) {
        List<Bicicleta> bicicletas = totemService.listarBicicletas(id);
        return ResponseEntity.ok(bicicletas);
    }
}
