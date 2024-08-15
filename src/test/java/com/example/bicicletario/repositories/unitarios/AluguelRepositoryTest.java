package com.example.bicicletario.repositories.unitarios;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class AluguelRepositoryTest {

    private AluguelRepository aluguelRepository;

    @BeforeEach
    void setUp() {
        aluguelRepository = new AluguelRepository();
    }

    @Test
    void testSaveAndFindById() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);

        // Act
        Aluguel savedAluguel = aluguelRepository.save(aluguel);

        // Assert
        Optional<Aluguel> retrievedAluguel = aluguelRepository.findByCiclistaAndHoraFimIsNull(1);
        assertTrue(retrievedAluguel.isPresent());
        assertEquals(savedAluguel.getId(), retrievedAluguel.get().getId());
    }

    @Test
    void testExistsByCiclistaAndHoraFimIsNull_Success() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);
        aluguelRepository.save(aluguel);

        // Act
        boolean exists = aluguelRepository.existsByCiclistaAndHoraFimIsNull(1);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByCiclistaAndHoraFimIsNull_NotFound() {
        // Act
        boolean exists = aluguelRepository.existsByCiclistaAndHoraFimIsNull(1);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testFindByCiclistaAndHoraFimIsNull_Success() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);
        aluguelRepository.save(aluguel);

        // Act
        Optional<Aluguel> retrievedAluguel = aluguelRepository.findByCiclistaAndHoraFimIsNull(1);

        // Assert
        assertTrue(retrievedAluguel.isPresent());
        assertEquals(aluguel.getId(), retrievedAluguel.get().getId());
    }

    @Test
    void testFindByCiclistaAndHoraFimIsNull_NotFound() {
        // Act
        Optional<Aluguel> retrievedAluguel = aluguelRepository.findByCiclistaAndHoraFimIsNull(1);

        // Assert
        assertFalse(retrievedAluguel.isPresent());
    }

    @Test
    void testDeleteAluguel() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);
        Aluguel savedAluguel = aluguelRepository.save(aluguel);

        // Act
        aluguelRepository.delete(savedAluguel);
        Optional<Aluguel> retrievedAluguel = aluguelRepository.findByCiclistaAndHoraFimIsNull(1);

        // Assert
        assertFalse(retrievedAluguel.isPresent());
    }

    @Test
    void testSaveAluguelWithHoraFim() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);
        aluguel.setHoraFim(String.valueOf(LocalDateTime.now()));
        aluguelRepository.save(aluguel);

        // Act
        Optional<Aluguel> retrievedAluguel = aluguelRepository.findByCiclistaAndHoraFimIsNull(1);

        // Assert
        assertFalse(retrievedAluguel.isPresent());
    }
}
