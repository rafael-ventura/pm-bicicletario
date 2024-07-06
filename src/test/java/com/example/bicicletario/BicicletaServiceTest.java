package com.example.bicicletario;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BicicletaServiceTest {

    @InjectMocks
    private BicicletaService bicicletaService;

    public BicicletaServiceTest(BicicletaRepository bicicletaRepository) {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listarBicicletas() {
        BicicletaRepository bicicletaRepository = Mockito.mock(BicicletaRepository.class);
        TrancaRepository trancaRepository = Mockito.mock(TrancaRepository.class);
        BicicletaMapper bicicletaMapper = Mockito.mock(BicicletaMapper.class);
        BicicletaService bicicletaService = new BicicletaService(bicicletaRepository, trancaRepository, bicicletaMapper);

        Bicicleta bicicleta = new Bicicleta("marca", "modelo", "2021", 1, StatusBicicleta.DISPONIVEL);
        when(bicicletaRepository.findAll()).thenReturn(List.of(bicicleta));

        List<Bicicleta> bicicletas = bicicletaService.listarBicicletas();
        assertEquals(1, bicicletas.size());
        assertEquals("marca", bicicletas.get(0).getMarca());
    }
}
