package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bicicletario.bicicletario.domain.constants.Constantes.*;

@RestController
@RequestMapping("/api/bicicleta")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping
    public ResponseEntity<List<Bicicleta>> listarBicicletas() {
        List<Bicicleta> bicicletas = bicicletaService.listarBicicletas();
        return ResponseEntity.ok(bicicletas);
    }

    @PostMapping
    public ResponseEntity<Bicicleta> criarBicicleta(@RequestBody NovaBicicletaDTO bicicleta) {
        Bicicleta bicicletaCadastrada = bicicletaService.criarBicicleta(bicicleta);
        return ResponseEntity.ok(bicicletaCadastrada);
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<String> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        bicicletaService.integrarNaRede(dto);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<String> retirarDaRede(@RequestBody RetirarBicicletaDaRedeDTO dto) {
        bicicletaService.retirarDaRede(dto);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<Bicicleta> obterBicicleta(@PathVariable Long idBicicleta) {
        Bicicleta bicicleta = bicicletaService.obterBicicleta(idBicicleta);
        return ResponseEntity.ok(bicicleta);
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<Bicicleta> editarBicicleta(@PathVariable Long idBicicleta, @RequestBody NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicletaEditada = bicicletaService.editarBicicleta(idBicicleta, bicicletaDTO);
        return ResponseEntity.ok(bicicletaEditada);
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<String> removerBicicleta(@PathVariable Long idBicicleta) {
        bicicletaService.removerBicicleta(idBicicleta);
        return ResponseEntity.ok(BICICLETA_REMOVIDA);
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<Bicicleta> alterarStatusBicicleta(@PathVariable Long idBicicleta, @PathVariable String acao) {
        Bicicleta bicicleta = bicicletaService.alterarStatusBicicleta(idBicicleta, acao);
        return ResponseEntity.ok(bicicleta);
    }
}
