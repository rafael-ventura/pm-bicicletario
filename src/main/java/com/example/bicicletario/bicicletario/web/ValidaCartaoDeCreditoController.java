package com.example.bicicletario.bicicletario.web;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.application.ValidaCartaoDeCreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validaCartaoDeCredito")
public class ValidaCartaoDeCreditoController {

    @Autowired
    private ValidaCartaoDeCreditoService validaCartaoDeCreditoService;

    @PostMapping
    public ResponseEntity<?> validarCartaoDeCredito(@RequestBody NovoCartaoDeCreditoDTO cartaoDeCredito) {
        boolean isValid = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);

        if (isValid) {
            return ResponseEntity.ok("Dados atualizados");
        } else {
            Erro erro = new Erro("422", "Dados Inválidos");
            return ResponseEntity.status(422).body(erro);
        }
    }
}
