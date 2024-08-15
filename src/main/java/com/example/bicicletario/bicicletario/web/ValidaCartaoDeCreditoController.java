package com.example.bicicletario.bicicletario.web;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.application.ValidaCartaoDeCreditoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validaCartaoDeCredito")
public class ValidaCartaoDeCreditoController {

    private static final Logger logger = LoggerFactory.getLogger(ValidaCartaoDeCreditoController.class);

    private final ValidaCartaoDeCreditoService validaCartaoDeCreditoService;

    @Autowired
    public ValidaCartaoDeCreditoController(ValidaCartaoDeCreditoService validaCartaoDeCreditoService) {
        this.validaCartaoDeCreditoService = validaCartaoDeCreditoService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validarCartaoDeCredito(@RequestBody NovoCartaoDeCreditoDTO cartaoDeCredito) {
        logger.info("Validando cartão de credito");
        boolean isValid = validaCartaoDeCreditoService.validarCartao(cartaoDeCredito);

        if (isValid) {
            logger.info("Cartão validado com sucesso");
            return ResponseEntity.status(200).body("Dados atualizados");
        } else {
            Erro erro = new Erro("422", "Dados Inválidos");
            logger.error("{} - {}", erro.getCodigo(), erro.getMensagem());
            return ResponseEntity.status(422).body(erro);
        }
    }
}
