package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.application.FilaCobrancaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FilaCobrancaController {

    private static final Logger logger = LoggerFactory.getLogger(FilaCobrancaController.class);

    @Autowired
    private FilaCobrancaService filaCobrancaService;

    @PostMapping("/filaCobranca")
    public ResponseEntity<?> adicionarNaFila(@RequestBody NovoCobrancaDTO novaCobranca) {
        try {
            logger.info("Adicionando cobranca na fila");
            Cobranca cobranca = filaCobrancaService.adicionarNaFila(novaCobranca);
            logger.info("Cobranca adicionada na fila");
            return ResponseEntity.ok(cobranca);
        } catch (IllegalArgumentException e) {
            Erro erro = new Erro("422", e.getMessage());
            logger.error(erro.getCodigo() + " - " + erro.getMensagem());
            return ResponseEntity.status(422).body(erro);
        } catch (Exception e) {
            Erro erro = new Erro("500", e.getMessage());
            logger.error(erro.getCodigo() + " - " + erro.getMensagem());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PostMapping("/processaCobrancasEmFila")
    public ResponseEntity<?> processarFila() {
        try {
            logger.info("Processando Cobrancas da fila");
            List<Cobranca> cobrancasProcessadas = filaCobrancaService.processarFila();
            logger.info("Cobrancas processadas com sucesso");
            return ResponseEntity.ok(cobrancasProcessadas);
        } catch (Exception e) {
            Erro erro = new Erro("422", e.getMessage());
            logger.error(erro.getCodigo() + " - " + erro.getMensagem());
            return ResponseEntity.status(422).body(erro);
        }
    }
}
