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
@RequestMapping("api/funcionario")
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
    public ResponseEntity<NovoFuncionarioDTO> cadastrarFuncionario(@RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        NovoFuncionarioDTO funcionarioDTO = funcionarioService.cadastrarFuncionario(novoFuncionarioDTO);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<NovoFuncionarioDTO> obterFuncionario(@PathVariable Long idFuncionario) {
        NovoFuncionarioDTO funcionarioDTO = funcionarioService.obterFuncionario(idFuncionario);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @PutMapping("/{idFuncionario}")
    public ResponseEntity<NovoFuncionarioDTO> alterarFuncionario(@PathVariable Long idFuncionario, @RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        NovoFuncionarioDTO funcionarioDTO = funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<?> excluirFuncionario(@PathVariable Long idFuncionario) {
        funcionarioService.excluirFuncionario(idFuncionario);
        return ResponseEntity.ok("Funcionário excluído com sucesso");
    }
}
