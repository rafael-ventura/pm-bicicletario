package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.CiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import com.example.bicicletario.bicicletario.mapper.CiclistaMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CiclistaService {

    private final CiclistaRepository ciclistaRepository;
    private final CiclistaMapper ciclistaMapper;
    private final AluguelRepository aluguelRepository;

    public CiclistaService(CiclistaRepository ciclistaRepository, CiclistaMapper ciclistaMapper, AluguelRepository aluguelRepository) {
        this.ciclistaRepository = ciclistaRepository;
        this.ciclistaMapper = ciclistaMapper;
        this.aluguelRepository = aluguelRepository;
    }

    public CiclistaDTO cadastrarCiclista(NovoCiclistaDTO novoCiclistaDTO) {
        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        validarCamposObrigatorios(novoCiclistaDTO);
        validarSenha(novoCiclistaDTO.getSenha(), novoCiclistaDTO.getConfirmacaoSenha());
        validarEmail(novoCiclistaDTO.getEmail());

        ciclista = ciclistaRepository.save(ciclista);
        enviarEmailConfirmacao(ciclista.getEmail());
        return ciclistaMapper.toDto(ciclista);
    }

    public Optional<CiclistaDTO> obterCiclista(Long idCiclista) {
        return ciclistaRepository.findById(idCiclista).map(ciclistaMapper::toDto);
    }

    public CiclistaDTO alterarCiclista(int idCiclista, NovoCiclistaDTO novoCiclistaDTO) {
        validarCamposObrigatorios(novoCiclistaDTO);
        validarSenha(novoCiclistaDTO.getSenha(), novoCiclistaDTO.getConfirmacaoSenha());
        validarEmail(novoCiclistaDTO.getEmail());
        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        ciclista.setId(idCiclista);

        ciclista = ciclistaRepository.save(ciclista);
        return ciclistaMapper.toDto(ciclista);
    }

    public CiclistaDTO ativarCiclista(Long idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        ciclista.setStatusCiclista(StatusCiclista.ATIVO);
        return ciclistaMapper.toDto(ciclistaRepository.save(ciclista));
    }

    public boolean permiteAluguel(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        return ciclista.getStatusCiclista() == StatusCiclista.ATIVO && !aluguelRepository.existsByCiclistaAndHoraFimIsNull(ciclista.getId());
    }

    public Optional<CiclistaDTO> obterBicicletaAlugada(Long idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        // Lógica para obter a bicicleta alugada pelo ciclista
        return Optional.of(ciclistaMapper.toDto(ciclista));
    }

    public boolean existeEmail(String email) {
        return ciclistaRepository.existsByEmail(email);
    }

    private void validarCamposObrigatorios(NovoCiclistaDTO novoCiclistaDTO) {
        // se tiver algum campo null retornar erro: todos os campos são obrigatórios
        if (novoCiclistaDTO.getNome() == null ||
                novoCiclistaDTO.getNome().isEmpty() ||
                novoCiclistaDTO.getEmail() == null ||
                novoCiclistaDTO.getEmail().isEmpty() ||
                novoCiclistaDTO.getNascimento() == null ||
                novoCiclistaDTO.getNascimento().isEmpty() ||
                novoCiclistaDTO.getNacionalidade() == null
        ) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios.");
        }

        if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.BRASILEIRO)) {
            if (novoCiclistaDTO.getCpf() == null || novoCiclistaDTO.getCpf().isEmpty()) {
                throw new IllegalArgumentException("CPF é obrigatório para brasileiros.");
            }
            // se for brasileiro, o CPF deve ser válido
            validarCPF(novoCiclistaDTO.getCpf());
        }
        if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.ESTRANGEIRO)) {
            if (novoCiclistaDTO.getPassaporte() == null || novoCiclistaDTO.getPassaporte() == null) {
                throw new IllegalArgumentException("Passaporte e País são obrigatórios para estrangeiros.");
            }
        }
    }

    private void validarSenha(String senha, String confirmacaoSenha) {
        if (senha == null || !senha.equals(confirmacaoSenha)) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
    }

    private void validarCPF(String cpf) {
        String cpfRegex = "\\d{11}";
        Pattern pattern = Pattern.compile(cpfRegex);
        if (cpf == null || !pattern.matcher(cpf).matches()) {
            throw new IllegalArgumentException("CPF inválido. O CPF deve conter 11 dígitos e apenas números.");
        }
    }

    private void validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null || !pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido.");
        }
        if (existeEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
    }

    private void enviarEmailConfirmacao(String email) {
        // Lógica para enviar email
        System.out.println("Email de confirmação enviado para: " + email);
    }
}
