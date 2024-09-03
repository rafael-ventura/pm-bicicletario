package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Constants;
import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.FuncionarioRepository;
import com.example.bicicletario.bicicletario.domain.mapper.FuncionarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private static final Logger log = LoggerFactory.getLogger(FuncionarioService.class);
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
    }

    public Funcionario cadastrarFuncionario(NovoFuncionarioDTO novoFuncionarioDTO) {
        validateFuncionarioDTO(novoFuncionarioDTO); // Validar campos obrigatórios

        Funcionario funcionario = new Funcionario();
        updateEntityWithDto(funcionario, novoFuncionarioDTO);

        funcionario.setMatricula(generateMatricula()); // Gera a matrícula automaticamente
        funcionario = funcionarioRepository.save(funcionario);
        return funcionario;
    }

    private void validateFuncionarioDTO(NovoFuncionarioDTO dto) {
        if (dto.getSenha() == null ||
                dto.getCpf() == null ||
                dto.getEmail() == null ||
                dto.getFuncao() == null ||
                dto.getIdade() == null ||
                dto.getNome() == null) {
            throw new InvalidDataException("Campos obrigatórios não preenchidos.");
        }
        validarSenha(dto.getSenha(), dto.getConfirmacaoSenha());
        validarCPF(dto.getCpf());
    }

    private void validarSenha(String senha, String confirmacaoSenha) {
        if (senha == null || !senha.equals(confirmacaoSenha)) {
            throw new InvalidDataException("As senhas não coincidem.");
        }
    }

    private void validarCPF(String cpf) {
        String cpfRegex = "\\d{11}";
        if (cpf == null || !cpf.matches(cpfRegex)) {
            throw new InvalidDataException("CPF inválido. O CPF deve conter 11 dígitos e apenas números.");
        }
    }

    public List<NovoFuncionarioDTO> listarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarioMapper.toDtoList(funcionarios);
    }

    public NovoFuncionarioDTO obterFuncionario(Integer idFuncionario) {
        Optional<NovoFuncionarioDTO> novoFuncionarioOpt = funcionarioRepository.findById(idFuncionario).map(funcionarioMapper::toDto);
        if (novoFuncionarioOpt.isEmpty()) {
            throw new ResourceNotFoundException(Constants.FUNCIONARIO_NAO_ENCONTRADO + idFuncionario);
        }
        return novoFuncionarioOpt.get();
    }

    public NovoFuncionarioDTO alterarFuncionario(Integer idFuncionario, NovoFuncionarioDTO novoFuncionarioDTO) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.FUNCIONARIO_NAO_ENCONTRADO + idFuncionario));

        // Atualiza somente os campos que foram fornecidos no DTO
        updateEntityWithDto(funcionario, novoFuncionarioDTO);

        // Salva o funcionário atualizado no banco de dados
        funcionarioRepository.save(funcionario);

        // Converte o funcionário atualizado para DTO e retorna
        return novoFuncionarioDTO;
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

    public void excluirFuncionario(Integer idFuncionario) {
        log.info("Excluindo funcionário com ID: {}", idFuncionario);
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.FUNCIONARIO_NAO_ENCONTRADO + idFuncionario));
        funcionarioRepository.delete(funcionario);
    }

    private String generateMatricula() {
        // Implementar lógica para gerar matrícula
        return "MAT-" + System.currentTimeMillis();
    }
}
