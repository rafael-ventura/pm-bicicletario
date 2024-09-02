package com.example.bicicletario.Unitario.Services;

import com.example.bicicletario.bicicletario.application.services.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

class FuncionarioServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   /* @Test
    void testGetFuncionario() {
        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setId(1);
        funcionarioMock.setNome("Funcionario");

        // Mockando a resposta do RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenReturn(new ResponseEntity<>(funcionarioMock, HttpStatus.OK));

        Funcionario funcionario = funcionarioService.get(1);

        assertNotNull(funcionario);
        assertEquals(1, funcionario.getId());
        assertEquals("Funcionario", funcionario.getNome());
    }

    @Test
    void testIsFuncionarioValido() {
        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setId(1);

        // Mockando a resposta do RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenReturn(new ResponseEntity<>(funcionarioMock, HttpStatus.OK));

        boolean isValido = funcionarioService.isFuncionarioValido(1);

        assertTrue(isValido);
    }

    @Test
    void testGetFuncionario_NotFound() {
        // Mockando uma exceção 404 do RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(Funcionario.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> {
            funcionarioService.get(1);
        });
    }*/
}
