package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.CobrancaService;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cobranca")
public class CobrancaController {

    private static final Logger logger = LoggerFactory.getLogger(CobrancaController.class);
    private static final String ERROR_LOG_FORMAT = "{} - {}";

    private final CobrancaService cobrancaService;

    @Autowired
    public CobrancaController(CobrancaService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @PostMapping
    public ResponseEntity<Object> realizarCobranca(@RequestBody NovoCobrancaDTO novaCobranca) {
        try {
            logger.info("Realizando cobranca");
            Cobranca cobranca = cobrancaService.realizarCobranca(novaCobranca);
            logger.info("cobranca realizada com sucesso");
            return ResponseEntity.status(200).body(cobranca);
        } catch (Exception e) {
            Erro erro = new Erro("422", "Dados Inválidos");
            logger.error(ERROR_LOG_FORMAT, erro.getCodigo(), erro.getMensagem());
            return ResponseEntity.status(422).body(erro);
        }
    }

    @GetMapping("/{idCobranca}")
    public ResponseEntity<Object>  obterCobranca(@PathVariable int idCobranca) {
        logger.info("Pegando cobranca");
        Cobranca cobranca = cobrancaService.obterCobrancaPorId(idCobranca);
        if (cobranca != null) {
            logger.info("Cobranca realizada com sucesso");
            return ResponseEntity.ok(cobranca);
        } else {
            Erro erro = new Erro("404", "Cobrança não encontrada");
            logger.error(ERROR_LOG_FORMAT, erro.getCodigo(), erro.getMensagem());
            return ResponseEntity.status(404).body(erro);
        }
    }

}
