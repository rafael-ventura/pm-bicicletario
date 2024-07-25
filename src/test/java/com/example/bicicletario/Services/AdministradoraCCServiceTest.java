import com.example.bicicletario.bicicletario.application.AdministradoraCCService;
import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AdministradoraCCServiceTest {

    @InjectMocks
    private AdministradoraCCService administradoraCCService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarParaAdministradoraCCComSucesso() {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setNumero("5269 2079 9840 6777");
        cartaoDeCredito.setValidade("23/05/2025");
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");

        BigDecimal valor = BigDecimal.valueOf(100);

        boolean resultado = administradoraCCService.enviarParaAdministradoraCC(cartaoDeCredito, valor);
        assertTrue(resultado);
    }

    @Test
    void enviarParaAdministradoraCCComErro() {
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setNumero(null);  // Simulando um valor inválido que cause exceção
        cartaoDeCredito.setValidade("23/05/2025");
        cartaoDeCredito.setCvv("707");
        cartaoDeCredito.setNomeTitular("JOAQUIM MAÇOMBO LEAO");

        BigDecimal valor = BigDecimal.valueOf(100);

        boolean resultado = administradoraCCService.enviarParaAdministradoraCC(cartaoDeCredito, valor);
        assertFalse(resultado);
    }
}
