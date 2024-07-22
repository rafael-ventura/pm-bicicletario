package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.ErroDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import com.example.bicicletario.bicicletario.exception.EmailAlreadyExistsException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import com.example.bicicletario.bicicletario.mapper.CiclistaMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CiclistaService {

    private final CiclistaRepository ciclistaRepository;
    private final CiclistaMapper ciclistaMapper;
    private final AluguelRepository aluguelRepository;
    private final AdministradoraCCService administradoraCCService; // Serviço para validação do cartão de crédito

    public CiclistaService(CiclistaRepository ciclistaRepository, CiclistaMapper ciclistaMapper, AluguelRepository aluguelRepository, AdministradoraCCService administradoraCCService) {
        this.ciclistaRepository = ciclistaRepository;
        this.ciclistaMapper = ciclistaMapper;
        this.aluguelRepository = aluguelRepository;
        this.administradoraCCService = administradoraCCService;
    }

    public Ciclista cadastrarCiclista(NovoCiclistaRequestDTO request) {
        NovoCiclistaDTO novoCiclistaDTO = request.getCiclista();
        CartaoDeCredito meioDePagamentoDTO = request.getMeioDePagamento();

        validarCamposObrigatorios(novoCiclistaDTO);
        validarSenha(novoCiclistaDTO.getSenha(), novoCiclistaDTO.getConfirmacaoSenha());
        validarEmail(novoCiclistaDTO.getEmail());

        // Validação do cartão de crédito junto à Administradora CC
        try {
            administradoraCCService.validarCartao(meioDePagamentoDTO, true);
        } catch (Exception e) {
            new ErroDTO("422", "Cartão de crédito inválido.");
        }

        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        ciclista = ciclistaRepository.save(ciclista);
        enviarEmailConfirmacao(ciclista.getEmail());
        return ciclista;
    }

    public Optional<Ciclista> obterCiclista(Long idCiclista) {
        return ciclistaRepository.findById(idCiclista);
    }

    public Ciclista alterarCiclista(int idCiclista, NovoCiclistaDTO novoCiclistaDTO) {
        validarCamposObrigatorios(novoCiclistaDTO);
        validarSenha(novoCiclistaDTO.getSenha(), novoCiclistaDTO.getConfirmacaoSenha());
        validarEmail(novoCiclistaDTO.getEmail());
        if (!ciclistaRepository.existsById((long) idCiclista)) {
            throw new InvalidDataException("Ciclista não encontrado com o ID: " + idCiclista);
        }
        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        ciclista.setId(idCiclista);

        ciclista = ciclistaRepository.save(ciclista);
        return ciclista;
    }

    public Ciclista ativarCiclista(Long idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        ciclista.setStatusCiclista(StatusCiclista.ATIVO);
        return ciclistaRepository.save(ciclista);
    }

    public boolean permiteAluguel(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        return ciclista.getStatusCiclista() == StatusCiclista.ATIVO && !aluguelRepository.existsByCiclistaAndHoraFimIsNull(ciclista.getId());
    }

    public Optional<Ciclista> obterBicicletaAlugada(Long idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        // Lógica para obter a bicicleta alugada pelo ciclista
        return Optional.of(ciclistaMapper.toDto(ciclista));
    }

    public boolean existeEmail(String email) {
        return ciclistaRepository.existsByEmail(email);
    }

    private void validarCamposObrigatorios(NovoCiclistaDTO novoCiclistaDTO) {
        if (novoCiclistaDTO.getNome() == null || novoCiclistaDTO.getNome().isEmpty() ||
                novoCiclistaDTO.getEmail() == null || novoCiclistaDTO.getEmail().isEmpty() ||
                novoCiclistaDTO.getNascimento() == null || novoCiclistaDTO.getNascimento().isEmpty() ||
                novoCiclistaDTO.getNacionalidade() == null) {
            throw new InvalidDataException("Todos os campos são obrigatórios.");
        }

        if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.BRASILEIRO)) {
            if (novoCiclistaDTO.getCpf() == null || novoCiclistaDTO.getCpf().isEmpty()) {
                throw new InvalidDataException("CPF é obrigatório para brasileiros.");
            }
            validarCPF(novoCiclistaDTO.getCpf());
        } else if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.ESTRANGEIRO)) {
            if (novoCiclistaDTO.getPassaporte() == null) {
                throw new InvalidDataException("Passaporte e País são obrigatórios para estrangeiros.");
            }
        }
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

    private void validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !email.matches(emailRegex)) {
            throw new InvalidDataException("Email inválido.");
        }
        if (ciclistaRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email já cadastrado.");
        }
    }

    private void enviarEmailConfirmacao(String email) {
        System.out.println("Email de confirmação enviado para: " + email);
        // Lógica para enviar email
    }
}
