package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.AluguelService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.dto.NovoAluguelDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aluguel")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<Aluguel> alugarBicicleta(@RequestBody NovoAluguelDTO novoAluguelDTO) {
        Aluguel aluguel = aluguelService.aluguel(novoAluguelDTO.getCiclista(), novoAluguelDTO.getTrancaInicio());
        return ResponseEntity.status(HttpStatus.CREATED).body(aluguel);
    }
}
