package com.example.bicicletario.Unitarios.Repositories;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.infraestructure.CobrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CobrancaRepositoryTest {

    private CobrancaRepository cobrancaRepository;

    @BeforeEach
    void setUp() {
        cobrancaRepository = new CobrancaRepository();
    }

    @Test
    void testSaveAndFindById() {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(0); // Set ID to 0 to simulate new entry

        Cobranca savedCobranca = cobrancaRepository.save(cobranca);

        Cobranca retrievedCobranca = cobrancaRepository.findById(savedCobranca.getId());
        assertNotNull(retrievedCobranca);
        assertEquals((Integer) savedCobranca.getId(), retrievedCobranca.getId());
    }


    @Test
    void testDeleteById() {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(0); // Set ID to 0 to simulate new entry

        Cobranca savedCobranca = cobrancaRepository.save(cobranca);
        cobrancaRepository.deleteById(savedCobranca.getId());

        Cobranca retrievedCobranca = cobrancaRepository.findById(savedCobranca.getId());
        assertNull(retrievedCobranca);
    }

    @Test
    void testFindAll() {
        Cobranca cobranca1 = new Cobranca();
        cobranca1.setId(0); // Set ID to 0 to simulate new entry
        cobrancaRepository.save(cobranca1);

        Cobranca cobranca2 = new Cobranca();
        cobranca2.setId(0); // Set ID to 0 to simulate new entry
        cobrancaRepository.save(cobranca2);

        Iterable<Cobranca> cobrancas = cobrancaRepository.findAll();
        assertNotNull(cobrancas);
        assertTrue(cobrancas.iterator().hasNext());
    }
}
