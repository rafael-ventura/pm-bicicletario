package com.example.bicicletario.repositories;

import com.example.bicicletario.bicicletario.domain.RegistroDevolucao;
import com.example.bicicletario.bicicletario.infraestructure.RegistroDevolucaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegistroDevolucaoRepositoryTest {

    private RegistroDevolucaoRepository registroDevolucaoRepository;

    @BeforeEach
    void setUp() {
        registroDevolucaoRepository = new RegistroDevolucaoRepository();
    }

    @Test
    void testSave_NewRegistroDevolucao() {
        // Arrange
        RegistroDevolucao registro = new RegistroDevolucao();

        // Act
        RegistroDevolucao savedRegistro = registroDevolucaoRepository.save(registro);

        // Assert
        assertNotNull(savedRegistro.getId());
        assertEquals(1, savedRegistro.getId()); // Como é o primeiro, o ID deve ser 1
    }

    @Test
    void testSave_ExistingRegistroDevolucao() {
        // Arrange
        RegistroDevolucao registro = new RegistroDevolucao();
        registro.setId(1); // Definindo um ID manualmente

        // Act
        RegistroDevolucao savedRegistro = registroDevolucaoRepository.save(registro);

        // Assert
        assertNotNull(savedRegistro.getId());
        assertEquals(1, savedRegistro.getId()); // O ID deve permanecer o mesmo
    }

    @Test
    void testSave_MultipleRegistros() {
        // Arrange
        RegistroDevolucao registro1 = new RegistroDevolucao();
        RegistroDevolucao registro2 = new RegistroDevolucao();

        // Act
        RegistroDevolucao savedRegistro1 = registroDevolucaoRepository.save(registro1);
        RegistroDevolucao savedRegistro2 = registroDevolucaoRepository.save(registro2);

        // Assert
        assertEquals(1, savedRegistro1.getId());
        assertEquals(2, savedRegistro2.getId()); // O segundo registro deve ter ID 2
    }

    @Test
    void testSave_UpdateExistingRegistro() {
        // Arrange
        RegistroDevolucao registro = new RegistroDevolucao();
        RegistroDevolucao savedRegistro = registroDevolucaoRepository.save(registro);

        // Act
        savedRegistro.setId(1);
        RegistroDevolucao updatedRegistro = registroDevolucaoRepository.save(savedRegistro);

        // Assert
        assertEquals(savedRegistro.getId(), updatedRegistro.getId());
        assertEquals(1, updatedRegistro.getId()); // O ID deve permanecer o mesmo
    }
}
