package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.application.FuncionarioService;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private FuncionarioMapper funcionarioMapper;

    @GetMapping
    public ResponseEntity<List<NovoFuncionarioDTO>> listarFuncionarios() {
        List<NovoFuncionarioDTO> funcionarios = funcionarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping
    public ResponseEntity<NovoFuncionarioDTO> cadastrarFuncionario(@RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        NovoFuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionarioService.cadastrarFuncionario(novoFuncionarioDTO));
        return ResponseEntity.ok(funcionarioDTO);
    }

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<NovoFuncionarioDTO> obterFuncionario(@PathVariable Integer idFuncionario) {
        NovoFuncionarioDTO funcionarioDTO = funcionarioService.obterFuncionario(idFuncionario);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @PutMapping("/{idFuncionario}")
    public ResponseEntity<NovoFuncionarioDTO> alterarFuncionario(@PathVariable Integer idFuncionario, @RequestBody NovoFuncionarioDTO novoFuncionarioDTO) {
        NovoFuncionarioDTO funcionarioDTO = funcionarioService.alterarFuncionario(idFuncionario, novoFuncionarioDTO);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<String> excluirFuncionario(@PathVariable Integer idFuncionario) {
        funcionarioService.excluirFuncionario(idFuncionario);
        return ResponseEntity.ok("Funcionário excluído com sucesso");
    }
}
