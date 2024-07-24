package com.example.bicicletario.bicicletario.web;
import com.example.bicicletario.bicicletario.application.EmailService;
import com.example.bicicletario.bicicletario.domain.Email;
import com.example.bicicletario.bicicletario.domain.Erro;
import com.example.bicicletario.bicicletario.domain.dto.NovoEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviarEmail")
    public ResponseEntity<?> enviarEmail(@RequestBody NovoEmailDTO novoEmailDTO) {
        try {
            System.out.println("Envio de email");
            Email email = emailService.enviarEmail(novoEmailDTO);
            return ResponseEntity.status(200).body("Externo solicitada");
        } catch (Exception e) {
            if (e.getMessage().contains("E-mail com formato inválido")) {
                Erro erro = new Erro("422", "E-mail com formato inválido");
                return ResponseEntity.status(422).body(erro);
            }
            Erro erro = new Erro("404", "E-mail não existe");
            return ResponseEntity.status(404).body(erro);
        }
    }
}
