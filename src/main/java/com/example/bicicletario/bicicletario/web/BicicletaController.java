package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.services.BicicletaService;
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
    public ResponseEntity<Bicicleta> cadastrarBicicleta(@RequestBody NovaBicicletaDTO bicicleta) {
        Bicicleta bicicletaCadastrada = bicicletaService.cadastrarBicicleta(bicicleta);
        return ResponseEntity.ok()
                .header(MESSAGE, DADOS_CADASTRADOS)
                .body(bicicletaCadastrada);
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<String> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        bicicletaService.integrarBicicletaNaRede(dto);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<String> retirarDaRede(@RequestBody RetirarBicicletaDaRedeDTO dto) {
        bicicletaService.retirarBicicletaDaRede(dto);
        return ResponseEntity.ok(DADOS_CADASTRADOS);
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<Bicicleta> obterBicicleta(@PathVariable Integer idBicicleta) {
        Bicicleta bicicleta = bicicletaService.obterBicicletaPorId(idBicicleta);
        return ResponseEntity.ok()
                .header(MESSAGE, DADOS_CADASTRADOS)
                .body(bicicleta);
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<Bicicleta> atualizarBicicleta(@PathVariable Integer idBicicleta, @RequestBody NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicletaEditada = bicicletaService.atualizarBicicleta(idBicicleta, bicicletaDTO);
        return ResponseEntity.ok()
                .header(MESSAGE, DADOS_CADASTRADOS)
                .body(bicicletaEditada);
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<String> removerBicicleta(@PathVariable Integer idBicicleta) {
        bicicletaService.excluirBicicleta(idBicicleta);
        return ResponseEntity.ok("Dados removidos");
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<Bicicleta> alterarStatusBicicleta(@PathVariable Integer idBicicleta, @PathVariable String acao) {
        Bicicleta bicicleta = bicicletaService.alterarStatusBicicleta(idBicicleta, acao);
        return ResponseEntity.ok()
                .header(MESSAGE, ACAO_BEM_SUCEDIDA)
                .body(bicicleta);
    }
}
