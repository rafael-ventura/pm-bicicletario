package com.example.bicicletario;

import com.example.bicicletario.bicicletario.application.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarBicicletaDaRedeDTO;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

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
    void listarBicicletas() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("marca");
        bicicleta.setModelo("modelo");
        bicicleta.setAno("2021");
        bicicleta.setNumero(1);
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);

        Mockito.when(bicicletaService.listarBicicletas()).thenReturn(List.of(bicicleta));

        mockMvc.perform(get("/api/bicicletas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'marca':'marca','modelo':'modelo','ano':'2021','numero':1,'status':'DISPONIVEL'}]"));
    }

    @Test
    void criarBicicleta() throws Exception {
        BicicletaDTO bicicletaDTO = new BicicletaDTO();
        bicicletaDTO.setStatus("NOVA");
        bicicletaDTO.setModelo("modelo");
        bicicletaDTO.setLocalizacao("localizacao");
        bicicletaDTO.setNumero(1);
        bicicletaDTO.setModelo("modelo");
        bicicletaDTO.setAnoDeFabricacao("2021");

        BicicletaDTO createdBicicletaDTO = new BicicletaDTO();
        createdBicicletaDTO.setStatus("NOVA");
        createdBicicletaDTO.setModelo("modelo");
        createdBicicletaDTO.setLocalizacao("localizacao");
        createdBicicletaDTO.setNumero(2);
        createdBicicletaDTO.setModelo("modelo");
        createdBicicletaDTO.setAnoDeFabricacao("2021");


        Mockito.when(bicicletaService.criarBicicleta(any(BicicletaDTO.class))).thenReturn(createdBicicletaDTO);

        mockMvc.perform(post("/api/bicicletas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NOVA\",\"modelo\":\"modelo\",\"localizacao\":\"localizacao\",\"numero\":1,\"modelo\":\"modelo\",\"anoDeFabricacao\":\"2021\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'NOVA','modelo':'modelo','localizacao':'localizacao','numero':2,'modelo':'modelo','anoDeFabricacao':'2021'}"));
    }

    @Test
    void integrarNaRede() throws Exception {
        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        mockMvc.perform(post("/api/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"));
    }

    @Test
    void retirarDaRede() throws Exception {
        RetirarBicicletaDaRedeDTO dto = new RetirarBicicletaDaRedeDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(1L);
        dto.setIdFuncionario(1L);

        mockMvc.perform(post("/api/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"idBicicleta\":1,\"idTranca\":1,\"idFuncionario\":1}"));
    }
}
