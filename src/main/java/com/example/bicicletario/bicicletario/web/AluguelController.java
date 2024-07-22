package com.example.bicicletario.bicicletario.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aluguel")
public class AluguelController {
    //TODO: implementar

    /*private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<?> realizarAluguel(@RequestBody NovoAluguelDTO novoAluguelDTO) {
        try {
            AluguelDTO aluguel = aluguelService.alugarBicicleta(novoAluguelDTO);
            return ResponseEntity.ok(aluguel);
        } catch (InvalidDataException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar aluguel");
        }
    }*/
}