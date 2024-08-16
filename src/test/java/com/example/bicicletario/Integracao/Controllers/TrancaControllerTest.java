//package com.example.bicicletario.Integracao.Controllers;
//
//import com.example.bicicletario.bicicletario.application.BicicletaService;
//import com.example.bicicletario.bicicletario.application.TrancaService;
//import com.example.bicicletario.bicicletario.application.exceptions.GlobalExceptionHandler;
//import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
//import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
//import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
//import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
//import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
//import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
//import com.example.bicicletario.bicicletario.domain.models.Tranca;
//import com.example.bicicletario.bicicletario.web.TrancaController;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class TrancaControllerTest {
//
//    @Mock
//    private TrancaService trancaService;
//
//    @Mock
//    private BicicletaService bicicletaService;
//
//    @InjectMocks
//    private TrancaController trancaController;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(trancaController)
//                .setControllerAdvice(new GlobalExceptionHandler())
//                .build();
//        this.objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void integrarNaRede() throws Exception {
//        IntegrarBicicletaNaRedeDTO dto = new IntegrarBicicletaNaRedeDTO();
//        dto.setIdBicicleta(1L);
//        dto.setIdTranca(1L);
//        dto.setIdFuncionario(1L);
//
//        mockMvc.perform(post("/api/tranca/integrarNaRede")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Dados cadastrados"));
//    }
//
//    @Test
//    void integrarNaRede_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Número da bicicleta inválido")).when(trancaService).incluirTrancaEmTotem(any(IntegrarBicicletaNaRedeDTO.class));
//
//        mockMvc.perform(post("/api/tranca/integrarNaRede")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new IntegrarBicicletaNaRedeDTO())))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Número da bicicleta inválido\"}"));
//    }
//
//    @Test
//    void integrarNaRede_ThrowsRuntimeException() throws Exception {
//        doThrow(new RuntimeException("Erro no envio do email")).when(trancaService).incluirTrancaEmTotem(any(IntegrarBicicletaNaRedeDTO.class));
//
//        mockMvc.perform(post("/api/tranca/integrarNaRede")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new IntegrarBicicletaNaRedeDTO())))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro no envio do email\"}"));
//    }
//
//    @Test
//    void retirarDaRede() throws Exception {
//        RetirarTrancaDaRedeDTO dto = new RetirarTrancaDaRedeDTO();
//        dto.setIdTranca(1L);
//        dto.setStatusAcaoReparador(StatusAcaoReparador.EM_REPARO);
//
//        mockMvc.perform(post("/api/tranca/retirarDaRede")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Dados cadastrados"));
//    }
//
//    @Test
//    void retirarDaRede_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Número da tranca inválido")).when(trancaService).retirarTrancaDaRede(any(RetirarTrancaDaRedeDTO.class));
//
//        mockMvc.perform(post("/api/tranca/retirarDaRede")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new RetirarTrancaDaRedeDTO())))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Número da tranca inválido\"}"));
//    }
//
//    @Test
//    void retirarDaRede_ThrowsRuntimeException() throws Exception {
//        doThrow(new RuntimeException("Erro no envio do email")).when(trancaService).retirarTrancaDaRede(any(RetirarTrancaDaRedeDTO.class));
//
//        mockMvc.perform(post("/api/tranca/retirarDaRede")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new RetirarTrancaDaRedeDTO())))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro no envio do email\"}"));
//    }
//
//    @Test
//    void listarTrancas() throws Exception {
//        Tranca tranca = new Tranca();
//        tranca.setId(1L);
//
//        when(trancaService.listarTodasTrancas()).thenReturn(List.of(tranca));
//
//        mockMvc.perform(get("/api/tranca"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(List.of(tranca))));
//    }
//
//    @Test
//    void listarTrancas_ThrowsException() throws Exception {
//        when(trancaService.listarTodasTrancas()).thenThrow(new RuntimeException("Erro ao listar trancas"));
//
//        mockMvc.perform(get("/api/tranca"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao listar trancas\"}"));
//    }
//
//    @Test
//    void cadastrarTranca() throws Exception {
//        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
//        trancaDTO.setLocalizacao("Localizacao");
//
//        Tranca tranca = new Tranca();
//        tranca.setId(1L);
//        tranca.setLocalizacao("Localizacao");
//
//        when(trancaService.cadastrarNovaTranca(any(NovaTrancaDTO.class))).thenReturn(tranca);
//
//        mockMvc.perform(post("/api/tranca")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(trancaDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(objectMapper.writeValueAsString(tranca)));
//    }
//
//    @Test
//    void cadastrarTranca_ThrowsInvalidDataException() throws Exception {
//        when(trancaService.cadastrarNovaTranca(any(NovaTrancaDTO.class))).thenThrow(new InvalidDataException("Dados inválidos"));
//
//        mockMvc.perform(post("/api/tranca")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovaTrancaDTO())))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void cadastrarTranca_ThrowsException() throws Exception {
//        when(trancaService.cadastrarNovaTranca(any(NovaTrancaDTO.class))).thenThrow(new RuntimeException("Erro ao criar tranca"));
//
//        mockMvc.perform(post("/api/tranca")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovaTrancaDTO())))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao criar tranca\"}"));
//    }
//
//    @Test
//    void obterTranca() throws Exception {
//        Tranca tranca = new Tranca();
//        tranca.setId(1L);
//
//        when(trancaService.obterTrancaPorId(1L)).thenReturn(tranca);
//
//        mockMvc.perform(get("/api/tranca/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(tranca)));
//    }
//
//    @Test
//    void obterTranca_ThrowsResourceNotFoundException() throws Exception {
//        when(trancaService.obterTrancaPorId(1L)).thenThrow(new ResourceNotFoundException("Tranca não encontrada"));
//
//        mockMvc.perform(get("/api/tranca/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Tranca não encontrada\"}"));
//    }
//
//    @Test
//    void obterTranca_ThrowsException() throws Exception {
//        when(trancaService.obterTrancaPorId(1L)).thenThrow(new RuntimeException("Erro ao obter tranca"));
//
//        mockMvc.perform(get("/api/tranca/1"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao obter tranca\"}"));
//    }
//
//    @Test
//    void editarTranca() throws Exception {
//        NovaTrancaDTO trancaDTO = new NovaTrancaDTO();
//        trancaDTO.setLocalizacao("Nova Localizacao");
//
//        Tranca tranca = new Tranca();
//        tranca.setId(1L);
//        tranca.setLocalizacao("Nova Localizacao");
//
//        when(trancaService.atualizarTranca(any(Long.class), any(NovaTrancaDTO.class))).thenReturn(tranca);
//
//        mockMvc.perform(put("/api/tranca/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(trancaDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(objectMapper.writeValueAsString(tranca)));
//    }
//
//    @Test
//    void editarTranca_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Dados inválidos")).when(trancaService).atualizarTranca(any(Long.class), any(NovaTrancaDTO.class));
//
//        mockMvc.perform(put("/api/tranca/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovaTrancaDTO())))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void editarTranca_ThrowsResourceNotFoundException() throws Exception {
//        doThrow(new ResourceNotFoundException("Tranca não encontrada")).when(trancaService).atualizarTranca(any(Long.class), any(NovaTrancaDTO.class));
//
//        mockMvc.perform(put("/api/tranca/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovaTrancaDTO())))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Tranca não encontrada\"}"));
//    }
//
//    @Test
//    void editarTranca_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao editar tranca")).when(trancaService).atualizarTranca(any(Long.class), any(NovaTrancaDTO.class));
//
//        mockMvc.perform(put("/api/tranca/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovaTrancaDTO())))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao editar tranca\"}"));
//    }
//
//    @Test
//    void removerTranca() throws Exception {
//        mockMvc.perform(delete("/api/tranca/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Tranca removida"));
//    }
//
//    @Test
//    void removerTranca_ThrowsResourceNotFoundException() throws Exception {
//        doThrow(new ResourceNotFoundException("Tranca não encontrada")).when(trancaService).excluirTranca(any(Long.class));
//
//        mockMvc.perform(delete("/api/tranca/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Tranca não encontrada\"}"));
//    }
//
//    @Test
//    void removerTranca_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao remover tranca")).when(trancaService).excluirTranca(any(Long.class));
//
//        mockMvc.perform(delete("/api/tranca/1"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao remover tranca\"}"));
//    }
//
//    @Test
//    void obterBicicletaNaTranca() throws Exception {
//        Tranca tranca = new Tranca();
//        tranca.setId(1L);
//
//        when(trancaService.obterBicicletaNaTranca(1L)).thenReturn(tranca);
//
//        mockMvc.perform(get("/api/tranca/1/bicicleta"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(tranca)));
//    }
//
//    @Test
//    void obterBicicletaNaTranca_ThrowsResourceNotFoundException() throws Exception {
//        when(trancaService.obterBicicletaNaTranca(any(Long.class))).thenThrow(new ResourceNotFoundException("Bicicleta não encontrada"));
//
//        mockMvc.perform(get("/api/tranca/1/bicicleta"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Bicicleta não encontrada\"}"));
//    }
//
//    @Test
//    void obterBicicletaNaTranca_ThrowsInvalidDataException() throws Exception {
//        when(trancaService.obterBicicletaNaTranca(any(Long.class))).thenThrow(new InvalidDataException("Dados inválidos"));
//
//        mockMvc.perform(get("/api/tranca/1/bicicleta"))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void obterBicicletaNaTranca_ThrowsException() throws Exception {
//        when(trancaService.obterBicicletaNaTranca(any(Long.class))).thenThrow(new RuntimeException("Erro ao obter bicicleta na tranca"));
//
//        mockMvc.perform(get("/api/tranca/1/bicicleta"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao obter bicicleta na tranca\"}"));
//    }
//
//    @Test
//    void trancarTranca() throws Exception {
//        mockMvc.perform(post("/api/tranca/1/trancar")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(1L)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Dados cadastrados"));
//    }
//
//    @Test
//    void trancarTranca_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Dados inválidos")).when(trancaService).trancarTranca(any(Long.class), any(Long.class));
//
//        mockMvc.perform(post("/api/tranca/1/trancar")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(1L)))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void trancarTranca_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao trancar tranca")).when(trancaService).trancarTranca(any(Long.class), any(Long.class));
//
//        mockMvc.perform(post("/api/tranca/1/trancar")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(1L)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao trancar tranca\"}"));
//    }
//
//    @Test
//    void destrancarTranca() throws Exception {
//        mockMvc.perform(post("/api/tranca/1/destrancar")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(1L)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Dados cadastrados"));
//    }
//
//    @Test
//    void destrancarTranca_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Dados inválidos")).when(trancaService).destrancarTranca(any(Long.class), any(Long.class));
//
//        mockMvc.perform(post("/api/tranca/1/destrancar")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(1L)))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void destrancarTranca_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao destrancar tranca")).when(trancaService).destrancarTranca(any(Long.class), any(Long.class));
//
//        mockMvc.perform(post("/api/tranca/1/destrancar")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(1L)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao destrancar tranca\"}"));
//    }
//
//    @Test
//    void alterarStatusTranca() throws Exception {
//        mockMvc.perform(post("/api/tranca/1/status/acao")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Dados cadastrados"));
//    }
//
//    @Test
//    void alterarStatusTranca_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Dados inválidos")).when(trancaService).alterarStatusTranca(any(Long.class), any(String.class));
//
//        mockMvc.perform(post("/api/tranca/1/status/acao")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void alterarStatusTranca_ThrowsResourceNotFoundException() throws Exception {
//        doThrow(new ResourceNotFoundException("Tranca não encontrada")).when(trancaService).alterarStatusTranca(any(Long.class), any(String.class));
//
//        mockMvc.perform(post("/api/tranca/1/status/acao")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Tranca não encontrada\"}"));
//    }
//
//    @Test
//    void alterarStatusTranca_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao alterar status da tranca")).when(trancaService).alterarStatusTranca(any(Long.class), any(String.class));
//
//        mockMvc.perform(post("/api/tranca/1/status/acao")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{\"codigo\":\"500\",\"mensagem\":\"Erro ao alterar status da tranca\"}"));
//    }
//}
