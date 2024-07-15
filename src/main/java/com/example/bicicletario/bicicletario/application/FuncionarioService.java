package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.ErroDTO;
import com.example.bicicletario.bicicletario.domain.dto.FuncionarioDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.infraestructure.FuncionarioRepository;
import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
    }

    public FuncionarioDTO cadastrarFuncionario(NovoFuncionarioDTO novoFuncionarioDTO) {
        Funcionario funcionario = funcionarioMapper.toEntity(novoFuncionarioDTO);
        // Gera a matrícula automaticamente
        funcionario.setMatricula(generateMatricula());
        funcionario = funcionarioRepository.save(funcionario);
        return funcionarioMapper.toDto(funcionario);
    }

    public List<FuncionarioDTO> listarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarioMapper.toDtoList(funcionarios);
    }

    public Optional<FuncionarioDTO> obterFuncionario(Long idFuncionario) {
        return funcionarioRepository.findById(idFuncionario)
                .map(funcionarioMapper::toDto);
    }

    public ResponseEntity<Object> alterarFuncionario(Long idFuncionario, NovoFuncionarioDTO novoFuncionarioDTO) {
        try {
            Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                    .orElseThrow(() -> new Exception("Funcionário não encontrado com o ID: " + idFuncionario));

            // Atualiza somente os campos que foram fornecidos no DTO
            updateEntityWithDto(funcionario, novoFuncionarioDTO);

            // Salva o funcionário atualizado no banco de dados
            funcionario = funcionarioRepository.save(funcionario);

            // Converte o funcionário atualizado para DTO e retorna
            return ResponseEntity.ok(funcionarioMapper.toDto(funcionario));
        } catch (Exception e) {
            ErroDTO erro = new ErroDTO("422", e.getMessage());
            return ResponseEntity.unprocessableEntity().body(erro);
        }
    }

    private void updateEntityWithDto(Funcionario funcionario, NovoFuncionarioDTO novoFuncionarioDTO) {
        if (novoFuncionarioDTO.getNome() != null) {
            funcionario.setNome(novoFuncionarioDTO.getNome());
        }
        if (novoFuncionarioDTO.getEmail() != null) {
            funcionario.setEmail(novoFuncionarioDTO.getEmail());
        }
        if (novoFuncionarioDTO.getCpf() != null) {
            funcionario.setCpf(novoFuncionarioDTO.getCpf());
        }
        if (novoFuncionarioDTO.getSenha() != null) {
            funcionario.setSenha(novoFuncionarioDTO.getSenha());
        }
        if (novoFuncionarioDTO.getConfirmacaoSenha() != null) {
            funcionario.setConfirmacaoSenha(novoFuncionarioDTO.getConfirmacaoSenha());
        }
        if (novoFuncionarioDTO.getIdade() != null) {
            funcionario.setIdade(novoFuncionarioDTO.getIdade());
        }
        if (novoFuncionarioDTO.getFuncao() != null) {
            funcionario.setFuncao(novoFuncionarioDTO.getFuncao());
        }
    }

    public void removerFuncionario(Long idFuncionario) {
        funcionarioRepository.deleteById(idFuncionario);
    }

    private String generateMatricula() {
        // Implementar lógica para gerar matrícula
        return "MAT-" + System.currentTimeMillis();
    }
}
