package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.domain.dto.FuncionarioDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.application.FuncionarioService;
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
    public ResponseEntity<List<FuncionarioDTO>> listarFuncionarios() {
        List<FuncionarioDTO> funcionarios = funcionarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> cadastrarFuncionario(@RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        FuncionarioDTO funcionarioDTO = funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<Object> obterFuncionario(@PathVariable Long idFuncionario) {
        Optional<FuncionarioDTO> funcionarioDTO = funcionarioService.obterFuncionario(idFuncionario);
        if (funcionarioDTO.isPresent()) {
            return ResponseEntity.ok(funcionarioDTO.get());
        } else {
            return ResponseEntity.status(404).body("Funcionário não encontrado");
        }
    }

    @PutMapping("/{idFuncionario}")
    public ResponseEntity<Object> alterarFuncionario(@PathVariable Long idFuncionario, @RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        return funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO);
    }

    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<Object> removerFuncionario(@PathVariable Long idFuncionario) {
        funcionarioService.removerFuncionario(idFuncionario);
        return ResponseEntity.ok("Funcionário removido com sucesso");
    }
}
