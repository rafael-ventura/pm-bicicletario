//package com.example.bicicletario.integracao;
//
//import com.example.bicicletario.bicicletario.application.EmailService;
//import com.example.bicicletario.bicicletario.domain.Email;
//import com.example.bicicletario.bicicletario.domain.Erro;
//import com.example.bicicletario.bicicletario.domain.dto.NovoEmailDTO;
//import com.example.bicicletario.bicicletario.web.EmailController;
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
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class EmailControllerTest {
//
//    @InjectMocks
//    private EmailController emailController;
//
//    @Mock
//    private EmailService emailService;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
//        this.objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void enviarEmailComSucesso() throws Exception {
//        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
//        Email email = new Email();
//
//        when(emailService.enviarEmail(any(NovoEmailDTO.class))).thenReturn(email);
//
//        mockMvc.perform(post("/api/enviarEmail")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(novoEmailDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(objectMapper.writeValueAsString(email)));
//    }
//
//    @Test
//    void enviarEmailComFormatoInvalido() throws Exception {
//        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
//
//        when(emailService.enviarEmail(any(NovoEmailDTO.class)))
//                .thenThrow(new RuntimeException("E-mail com formato invalido"));
//
//        mockMvc.perform(post("/api/enviarEmail")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(novoEmailDTO)))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("422", "E-mail com formato invalido"))));
//    }
//
//    @Test
//    void enviarEmailNaoExiste() throws Exception {
//        NovoEmailDTO novoEmailDTO = new NovoEmailDTO();
//
//        when(emailService.enviarEmail(any(NovoEmailDTO.class)))
//                .thenThrow(new RuntimeException("E-mail nao existe"));
//
//        mockMvc.perform(post("/api/enviarEmail")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(novoEmailDTO)))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(objectMapper.writeValueAsString(new Erro("404", "E-mail nao existe"))));
//    }
//}
