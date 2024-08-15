//package com.example.bicicletario.controllers;
//
//import com.example.bicicletario.bicicletario.application.CiclistaService;
//import com.example.bicicletario.bicicletario.domain.Bicicleta;
//import com.example.bicicletario.bicicletario.domain.Ciclista;
//import com.example.bicicletario.bicicletario.domain.Passaporte;
//import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
//import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
//import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
//import com.example.bicicletario.bicicletario.domain.dto.PassaporteDTO;
//import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
//import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
//import com.example.bicicletario.bicicletario.exception.GlobalExceptionHandler;
//import com.example.bicicletario.bicicletario.exception.InvalidDataException;
//import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
//import com.example.bicicletario.bicicletario.web.CiclistaController;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class CiclistaControllerTest {
//
//    @InjectMocks
//    private CiclistaController ciclistaController;
//
//    @Mock
//    private CiclistaService ciclistaService;
//
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(ciclistaController)
//                .setControllerAdvice(new GlobalExceptionHandler())
//                .build();
//
//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        this.objectMapper.findAndRegisterModules();
//    }
//
//
//    @Test
//    void criarCiclista() throws Exception {
//        // Configurar DTOs e Ciclista
//        PassaporteDTO passaporteDTO = new PassaporteDTO();
//        passaporteDTO.setNumero("123456");
//        passaporteDTO.setValidade("2025-01-01");
//        passaporteDTO.setPais("Brasil");
//
//        Passaporte passaporte = new Passaporte();
//        passaporte.setNumero("123456");
//        passaporte.setValidade("2025-01-01");
//        passaporte.setPais("Brasil");
//
//        NovoCartaoDeCreditoDTO cartao = new NovoCartaoDeCreditoDTO();
//        cartao.setNomeTitular("Joao Silva");
//        cartao.setNumero("1234567890123456");
//        cartao.setValidade("2025-01-01");
//        cartao.setCvv("123");
//
//        NovoCiclistaDTO novoCiclista = new NovoCiclistaDTO();
//        novoCiclista.setNome("Joao Silva");
//        novoCiclista.setCpf("12345678900");
//        novoCiclista.setEmail("joao.silva@example.com");
//        novoCiclista.setNascimento("2000-01-01");
//        novoCiclista.setNacionalidade(Nacionalidade.BRASILEIRO);
//        novoCiclista.setUrlFotoDocumento("http://example.com/foto.jpg");
//        novoCiclista.setPassaporte(passaporteDTO);
//
//        NovoCiclistaRequestDTO dto = new NovoCiclistaRequestDTO();
//        dto.setMeioDePagamento(cartao);
//        dto.setCiclista(novoCiclista);
//
//        Ciclista ciclista = new Ciclista();
//        ciclista.setId(1);
//        ciclista.setNome("Joao Silva");
//        ciclista.setCpf("12345678900");
//        ciclista.setEmail("joao.silva@example.com");
//        ciclista.setNascimento("2000-01-01");
//        ciclista.setNacionalidade(Nacionalidade.BRASILEIRO);
//        ciclista.setUrlFotoDocumento("http://example.com/foto.jpg");
//        ciclista.setPassaporte(passaporte);
//        ciclista.setStatus(StatusCiclista.ATIVO);
//
//        when(ciclistaService.cadastrarCiclista(any(NovoCiclistaRequestDTO.class))).thenReturn(ciclista);
//
//        String expectedJson = objectMapper.writeValueAsString(ciclista);
//        System.out.println("Expected JSON: " + expectedJson);
//
//        mockMvc.perform(post("/api/ciclista")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(result -> {
//                    String actualJson = result.getResponse().getContentAsString();
//                    System.out.println("Actual JSON: " + actualJson);
//
//                    // Normalizar e comparar JSONs
//                    String normalizedExpectedJson = normalizeJson(expectedJson);
//                    String normalizedActualJson = normalizeJson(actualJson);
//
//                    assertEquals(normalizedExpectedJson, normalizedActualJson);
//                });
//    }
//
//
//    private String normalizeJson(String json) {
//        return json.replaceAll("[\\r\\n\\t]", "").replaceAll("\\s+", " ");
//    }
//
//
//    @Test
//    void criarCiclista_ThrowsInvalidDataException() throws Exception {
//        doThrow(new InvalidDataException("Dados inválidos")).when(ciclistaService).cadastrarCiclista(any(NovoCiclistaRequestDTO.class));
//
//        mockMvc.perform(post("/api/ciclista")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
//                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.content().json("{\"codigo\":\"422\",\"mensagem\":\"Dados inválidos\"}"));
//    }
//
//    @Test
//    void criarCiclista_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao criar ciclista")).when(ciclistaService).cadastrarCiclista(any(NovoCiclistaRequestDTO.class));
//
//        mockMvc.perform(post("/api/ciclista")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao criar ciclista'}"));
//    }
//
//    @Test
//    void obterCiclista() throws Exception {
//        Ciclista ciclista = new Ciclista();
//        ciclista.setId(1);
//        ciclista.setNome("Joao Silva");
//
//        when(ciclistaService.obterCiclista(1)).thenReturn(Optional.of(ciclista));
//
//        mockMvc.perform(get("/api/ciclista/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{'id':1,'nome':'Joao Silva'}"));
//    }
//
//    @Test
//    void obterCiclista_ThrowsResourceNotFoundException() throws Exception {
//        when(ciclistaService.obterCiclista(1)).thenThrow(new ResourceNotFoundException("Ciclista não encontrado"));
//
//        mockMvc.perform(get("/api/ciclista/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{'codigo':'404','mensagem':'Ciclista não encontrado'}"));
//    }
//
//    @Test
//    void editarCiclista() throws Exception {
//        Ciclista ciclista = new Ciclista();
//        ciclista.setId(1);
//        ciclista.setNome("Joao Silva");
//
//        when(ciclistaService.alterarCiclista(any(Integer.class), any(NovoCiclistaRequestDTO.class))).thenReturn(ciclista);
//
//        mockMvc.perform(put("/api/ciclista/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{'id':1,'nome':'Joao Silva'}"));
//    }
//
//    @Test
//    void editarCiclista_ThrowsResourceNotFoundException() throws Exception {
//        // Configura o comportamento esperado do serviço
//        doThrow(new ResourceNotFoundException("Ciclista não encontrado")).when(ciclistaService).alterarCiclista(any(Integer.class), any(NovoCiclistaRequestDTO.class));
//
//        // Realiza a requisição e verifica a resposta
//        mockMvc.perform(put("/api/ciclista/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
//                .andExpect(status().isNotFound()) // Verifica o status 404
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica o tipo de conteúdo
//                .andExpect(content().json("{\"codigo\":\"404\",\"mensagem\":\"Ciclista não encontrado\"}")); // Verifica o conteúdo JSON da resposta
//    }
//
//    @Test
//    void editarCiclista_ThrowsException() throws Exception {
//        doThrow(new RuntimeException("Erro ao editar ciclista")).when(ciclistaService).alterarCiclista(any(Integer.class), any(NovoCiclistaRequestDTO.class));
//
//        mockMvc.perform(put("/api/ciclista/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new NovoCiclistaDTO())))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().json("{'codigo':'500','mensagem':'Erro ao editar ciclista'}"));
//    }
//
//    @Test
//    void ativarCiclistaComSucesso() throws Exception {
//        Ciclista ciclista = new Ciclista();
//        ciclista.setId(1);
//        ciclista.setNome("Joao Silva");
//
//        when(ciclistaService.ativarCiclista(anyInt())).thenReturn(ciclista);
//
//        mockMvc.perform(post("/api/ciclista/1/ativar")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("{\"id\":1,\"nome\":\"Joao Silva\"}"));
//    }
//
//    @Test
//    void ativarCiclista_ThrowsResourceNotFoundException() throws Exception {
//        doThrow(new ResourceNotFoundException("Ciclista não encontrado")).when(ciclistaService).ativarCiclista(1);
//
//        mockMvc.perform(post("/api/ciclista/1/ativar"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json("{'codigo':'404','mensagem':'Ciclista não encontrado'}"));
//    }
//
//    // Teste para permite aluguel
//    @Test
//    void permiteAluguel_True() throws Exception {
//        when(ciclistaService.permiteAluguel(1)).thenReturn(true);
//
//        mockMvc.perform(get("/api/ciclista/1/permiteAluguel"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void permiteAluguel_False() throws Exception {
//        when(ciclistaService.permiteAluguel(1)).thenReturn(false);
//
//        mockMvc.perform(get("/api/ciclista/1/permiteAluguel"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//    }
//
//    // Teste para obter bicicleta alugada
//    @Test
//    void obterBicicletaAlugada_Sucesso() throws Exception {
//        Bicicleta bicicleta = new Bicicleta();
//        bicicleta.setId(1);
//
//        when(ciclistaService.obterBicicletaAlugada(1)).thenReturn(Optional.of(bicicleta));
//
//        mockMvc.perform(get("/api/ciclista/1/bicicletaAlugada"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{'id':1}"));
//    }
//
//    @Test
//    void obterBicicletaAlugada_Vazio() throws Exception {
//        when(ciclistaService.obterBicicletaAlugada(1)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/ciclista/1/bicicletaAlugada"))
//                .andExpect(status().isNoContent());
//    }
//
//    // Teste para verificar existência de email
//    @Test
//    void existeEmail_True() throws Exception {
//        when(ciclistaService.existeEmail("joao.silva@example.com")).thenReturn(true);
//
//        mockMvc.perform(get("/api/ciclista/existeEmail/joao.silva@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void existeEmail_False() throws Exception {
//        when(ciclistaService.existeEmail("joao.silva@example.com")).thenReturn(false);
//
//        mockMvc.perform(get("/api/ciclista/existeEmail/joao.silva@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//    }
//}
