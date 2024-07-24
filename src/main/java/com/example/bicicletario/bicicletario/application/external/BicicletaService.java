package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import org.springframework.stereotype.Service;

@Service
public class BicicletaService {
    public Bicicleta getBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2021");
        bicicleta.setNumero(1);
        return bicicleta;
    }
}
