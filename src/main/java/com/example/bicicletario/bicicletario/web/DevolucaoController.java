/*package com.example.bicicletario.bicicletario.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devolucao")
@Tag(name = "Aluguel", description = "Endpoints para gestão de aluguel e devolução de bicicletas")
public class DevolucaoController {

   private final CiclistaService ciclistaService;

    public DevolucaoAluguelController(CiclistaService ciclistaService) {
        this.ciclistaService = ciclistaService;
    }

    @PostMapping
    public ResponseEntity<DevolucaoDTO> realizarDevolucao(@RequestBody NovoDevolucaoDTO novoDevolucaoDTO) {
        DevolucaoDTO devolucao = ciclistaService.realizarDevolucao(novoDevolucaoDTO);
        return ResponseEntity.ok(devolucao);
    }

}*/
