//package com.example.bicicletario.repositories;
//
//import com.example.bicicletario.bicicletario.domain.Ciclista;
//import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CiclistaRepositoryTest {
//
//    private CiclistaRepository ciclistaRepository;
//
//    @BeforeEach
//    void setUp() {
//        ciclistaRepository = new CiclistaRepository();
//    }
//
//    @Test
//    void testSaveAndFindById() {
//        Ciclista ciclista = new Ciclista();
//        ciclista.setId(1);
//        ciclista.setEmail("test@example.com");
//
//        ciclistaRepository.save(ciclista);
//
//        Optional<Ciclista> result = ciclistaRepository.findById(1);
//        assertTrue(result.isPresent());
//        assertEquals(ciclista, result.get());
//    }
//
//    @Test
//    void testExistsByEmail() {
//        Ciclista ciclista = new Ciclista();
//        ciclista.setId(1);
//        ciclista.setEmail("test@example.com");
//
//        ciclistaRepository.save(ciclista);
//
//        boolean result = ciclistaRepository.existsByEmail("test@example.com");
//        assertTrue(result);
//    }
//
//    @Test
//    void testExistsByEmail_NotFound() {
//        boolean result = ciclistaRepository.existsByEmail("notfound@example.com");
//        assertFalse(result);
//    }
//}
