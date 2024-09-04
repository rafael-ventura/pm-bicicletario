package com.example.bicicletario.models;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrancaTest {

    @Test
    void testTranca() {
        Tranca tranca = new Tranca();
        Totem totem = new Totem();
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1);

        tranca.setId(1);
        tranca.setBicicleta(bicicleta);
        tranca.setNumero(123);
        tranca.setLocalizacao("Centro, Rio de Janeiro");
        tranca.setAnoDeFabricacao("2020");
        tranca.setModelo("Modelo X");
        tranca.setStatus(StatusTranca.LIVRE);
        tranca.setDataInsercaoTotem("2024-01-01");
        tranca.setDataRemocaoTotem("2024-12-31");
        tranca.setIdFuncionarioUltimaOperacao(1001);

        assertEquals(1, tranca.getId());
        assertEquals(123, tranca.getNumero());
        assertEquals("Centro, Rio de Janeiro", tranca.getLocalizacao());
        assertEquals("2020", tranca.getAnoDeFabricacao());
        assertEquals("Modelo X", tranca.getModelo());
        assertEquals(StatusTranca.LIVRE, tranca.getStatus());
        assertEquals("2024-01-01", tranca.getDataInsercaoTotem());
        assertEquals("2024-12-31", tranca.getDataRemocaoTotem());
        assertEquals(1001, tranca.getIdFuncionarioUltimaOperacao());
    }
}
