package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping("/bicicletas")
    public ResponseEntity<?> listarBicicletas() {
        try {
            return ResponseEntity.ok(bicicletaService.listarBicicletas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/bicicletas")
    public ResponseEntity<?> criarBicicleta(@RequestBody NovaBicicletaDTO bicicleta) {
        try {
            return ResponseEntity.ok(bicicletaService.criarBicicleta(bicicleta));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/bicicleta/integrarNaRede")
    public ResponseEntity<?> integrarNaRede(@RequestBody IntegrarBicicletaNaRedeDTO dto) {
        try {
            bicicletaService.integrarNaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/bicicleta/retirarDaRede")
    public ResponseEntity<?> retirarDaRede(@RequestBody RetirarBicicletaDaRedeDTO dto) {
        try {
            bicicletaService.retirarDaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }
}
