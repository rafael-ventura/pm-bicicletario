package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.DevolucaoService;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.domain.dto.NovoDevolucaoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devolucao")
public class DevolucaoController {

    private final DevolucaoService devolucaoService;

    public DevolucaoController(DevolucaoService devolucaoService) {
        this.devolucaoService = devolucaoService;
    }

    @PostMapping
    public ResponseEntity<Devolucao> realizarDevolucao(@RequestBody NovoDevolucaoDTO devolucaoDTO) {
        Devolucao devolucao = devolucaoService.realizarDevolucao(devolucaoDTO);
        return ResponseEntity.ok(devolucao);
    }
}
