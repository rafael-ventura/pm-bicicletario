package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.ErroDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<NovoFuncionarioDTO>> listarFuncionarios() {
        List<NovoFuncionarioDTO> funcionarios = funcionarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarFuncionario(@RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        try {
            NovoFuncionarioDTO funcionarioDTO = funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);
            return ResponseEntity.ok(funcionarioDTO);
        } catch (InvalidDataException e) {
            ErroDTO erroDTO = new ErroDTO("422", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroDTO);
        }
    }

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<Object> obterFuncionario(@PathVariable Long idFuncionario) {
        Optional<NovoFuncionarioDTO> funcionarioDTO = funcionarioService.obterFuncionario(idFuncionario);
        return funcionarioDTO.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body("Funcionário não encontrado"));
    }

    @PutMapping("/{idFuncionario}")
    public ResponseEntity<?> alterarFuncionario(@PathVariable Long idFuncionario, @RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        try {
            NovoFuncionarioDTO funcionarioDTO = funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO);
            return ResponseEntity.ok(funcionarioDTO);
        } catch (InvalidDataException e) {
            ErroDTO erroDTO = new ErroDTO("422", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroDTO);
        }
    }

    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<?> excluirFuncionario(@PathVariable Long idFuncionario) {
        try {
            funcionarioService.excluirFuncionario(idFuncionario);
            return ResponseEntity.ok("Funcionário excluído com sucesso");
        } catch (InvalidDataException e) {
            ErroDTO erroDTO = new ErroDTO("422", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroDTO);
        }
    }
}
