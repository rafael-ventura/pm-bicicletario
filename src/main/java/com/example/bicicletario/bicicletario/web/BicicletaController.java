package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.enums.Status;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return List.of(new Bicicleta("marca", "modelo", "2021", 1, Status.DISPONIVEL));

    }

    @PostMapping("/bicicletas")
    public Bicicleta criarBicicleta(Bicicleta bicicleta) {

        return bicicletaService.criarBicicleta(bicicleta);
    }

}



