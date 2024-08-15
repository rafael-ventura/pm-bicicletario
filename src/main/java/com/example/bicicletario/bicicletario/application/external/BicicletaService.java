package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BicicletaService {
    public Optional<Bicicleta> getBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2021");
        bicicleta.setNumero(1);
        return Optional.of(bicicleta);
    }

    public void atualizarStatus(Bicicleta bicicleta, StatusBicicleta status) {
        bicicleta.setStatusBicicleta(status);
    }

}
