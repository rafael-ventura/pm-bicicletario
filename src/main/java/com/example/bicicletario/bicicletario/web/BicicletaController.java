package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarBicicletaDaRedeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping("/bicicletas")
    public ResponseEntity<List<Bicicleta>> listarBicicletas() {
        try {
            return ResponseEntity.ok(bicicletaService.listarBicicletas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/bicicletas")
    public ResponseEntity<BicicletaDTO> criarBicicleta(@RequestBody BicicletaDTO bicicleta) {
        try {
            return ResponseEntity.ok(bicicletaService.criarBicicleta(bicicleta));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/bicicleta/integrarNaRede")
    public ResponseEntity<IntegrarBicicletaNaRedeDTO> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        try {
            bicicletaService.integrarNaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/bicicleta/retirarDaRede")
    public ResponseEntity<RetirarBicicletaDaRedeDTO> retirarDaRede(@RequestBody RetirarBicicletaDaRedeDTO dto) {
        try {
            bicicletaService.retirarDaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
