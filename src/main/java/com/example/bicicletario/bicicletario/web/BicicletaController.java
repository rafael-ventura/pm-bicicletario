package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.dto.IntegrarNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarDaRedeDTO;
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
    public List<Bicicleta> listarBicicletas() {
        return bicicletaService.listarBicicletas();
    }

    @PostMapping("/bicicletas")
    public Bicicleta criarBicicleta(Bicicleta bicicleta) {
        return bicicletaService.criarBicicleta(bicicleta);
    }

    @PostMapping("/bicicleta/integrarNaRede")
    public ResponseEntity<IntegrarNaRedeDTO> integrarNaRede(@RequestBody IntegrarNaRedeDTO dto) {
        try {
            bicicletaService.integrarNaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @PostMapping("/bicicleta/retirarDaRede")
    public ResponseEntity<RetirarDaRedeDTO> retirarDaRede(@RequestBody RetirarDaRedeDTO dto) {
        try {
            bicicletaService.retirarDaRede(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

}



