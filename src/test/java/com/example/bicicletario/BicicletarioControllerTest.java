package com.example.bicicletario;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.Status;
import com.example.bicicletario.bicicletario.web.BicicletaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BicicletarioControllerTest {

    @Mock
    private BicicletaService bicicletaService;

    @InjectMocks
    private BicicletaController bicicletaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bicicletaController).build();
    }

    @Test
    public void listarBicicletas() throws Exception {
        Bicicleta bicicleta = new Bicicleta("marca", "modelo", "2021", 1, Status.DISPONIVEL);
        Mockito.when(bicicletaService.listarBicicletas()).thenReturn(List.of(bicicleta));

        mockMvc.perform(get("/api/bicicletas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'marca':'marca','modelo':'modelo','ano':'2021','numero':1,'status':'DISPONIVEL'}]"));
    }
}
