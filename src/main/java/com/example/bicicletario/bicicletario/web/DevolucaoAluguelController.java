package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CiclistaService;
import com.example.bicicletario.bicicletario.domain.dto.DevolucaoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devolucao")
@Tag(name = "Aluguel", description = "Endpoints para gestão de aluguel e devolução de bicicletas")
public class DevolucaoAluguelController {

    private final CiclistaService ciclistaService;

    public DevolucaoAluguelController(CiclistaService ciclistaService) {
        this.ciclistaService = ciclistaService;
    }

    @PostMapping
    public ResponseEntity<DevolucaoDTO> realizarDevolucao(@RequestBody NovoDevolucaoDTO novoDevolucaoDTO) {
        DevolucaoDTO devolucao = ciclistaService.realizarDevolucao(novoDevolucaoDTO);
        return ResponseEntity.ok(devolucao);
    }

}
