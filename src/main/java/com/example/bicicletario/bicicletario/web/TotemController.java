package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.services.TotemService;
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
        List<Totem> totens = totemService.listarTodosTotens();
        return ResponseEntity.ok().body(totens);
    }

    @PostMapping
    public ResponseEntity<Totem> criarTotem(@RequestBody NovoTotemDTO totem) {
        Totem totemCadastrado = totemService.cadastrarNovoTotem(totem);
        return ResponseEntity.ok()
                .header(MESSAGE, DADOS_CADASTRADOS)
                .body(totemCadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Totem> editarTotem(@PathVariable Integer id, @RequestBody NovoTotemDTO totemDTO) {
        Totem totemEditado = totemService.atualizarTotem(id, totemDTO);
        return ResponseEntity.ok()
                .header(MESSAGE, DADOS_CADASTRADOS)
                .body(totemEditado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerTotem(@PathVariable Integer id) {
        totemService.excluirTotem(id);
        return ResponseEntity.ok(TOTEM_REMOVIDO);
    }

    @GetMapping("/{id}/trancas")
    public ResponseEntity<List<Tranca>> listarTrancas(@PathVariable Integer id) {
        List<Tranca> trancas = totemService.listarTrancasPorTotem(id);
        return ResponseEntity.ok(trancas);
    }

    @GetMapping("/{id}/bicicletas")
    public ResponseEntity<List<Bicicleta>> listarBicicletas(@PathVariable Integer id) {
        List<Bicicleta> bicicletas = totemService.listarBicicletasPorTotem(id);
        return ResponseEntity.ok(bicicletas);
    }
}
