package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import org.springframework.stereotype.Service;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository) {
        this.bicicletaRepository = bicicletaRepository;
    }

    public Bicicleta criarBicicleta(Bicicleta bicicleta) {
        return bicicletaRepository.criarBicicleta(bicicleta);
    }
}
