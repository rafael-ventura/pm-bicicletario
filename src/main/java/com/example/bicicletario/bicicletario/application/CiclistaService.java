package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import com.example.bicicletario.bicicletario.mapper.CiclistaMapper;
import com.example.bicicletario.bicicletario.mapper.PassaporteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CiclistaService {

    private static final Logger logger = LoggerFactory.getLogger(CiclistaService.class);
    private final CiclistaRepository ciclistaRepository;
    private final CiclistaMapper ciclistaMapper;
    private final AluguelRepository aluguelRepository;
    private final BicicletaService bicicletaService;
    private final CartaoDeCreditoService cartaoDeCreditoService;
    private final EmailService emailService;
    private final PassaporteMapper passaporteMapper;

    public CiclistaService(CiclistaRepository ciclistaRepository, CiclistaMapper ciclistaMapper, AluguelRepository aluguelRepository, BicicletaService bicicletaService, CartaoDeCreditoService cartaoDeCreditoService, EmailService emailService, PassaporteMapper passaporteMapper) {
        this.ciclistaRepository = ciclistaRepository;
        this.ciclistaMapper = ciclistaMapper;
        this.aluguelRepository = aluguelRepository;
        this.bicicletaService = bicicletaService;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
        this.emailService = emailService;
        this.passaporteMapper = passaporteMapper;
    }

    public Ciclista cadastrarCiclista(NovoCiclistaRequestDTO request) throws BadRequestException {
        logger.info("Cadastrando novo ciclista");
        validarCiclistaDto(request);

        NovoCiclistaDTO novoCiclistaDTO = request.getCiclista();
        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        ciclistaRepository.save(ciclista);

        cartaoDeCreditoService.save(request.getMeioDePagamento(), ciclista.getId());

        emailService.enviarEmailConfirmacao(ciclista);
        logger.info("Ciclista cadastrado com sucesso!");
        return ciclista;
    }

    private void validarCiclistaDto(NovoCiclistaRequestDTO novoCiclistaRequest) {
        validarCamposObrigatorios(novoCiclistaRequest.getCiclista());
        validarEmail(novoCiclistaRequest.getCiclista().getEmail());
        cartaoDeCreditoService.validarCartaoDeCredito(novoCiclistaRequest.getMeioDePagamento());
    }

    private void validarCiclistaParaAlterar(Ciclista ciclista) {
        if (ciclista.getSenha() != null || ciclista.getConfirmacaoSenha() != null) {
            validarSenhasIdenticas(ciclista.getSenha(), ciclista.getConfirmacaoSenha());
        }

        if (ciclista.getEmail() != null) {
            validarEmail(ciclista.getEmail());
        }

        if (ciclista.getNacionalidade() == Nacionalidade.BRASILEIRO) {
            if (ciclista.getCpf() == null || ciclista.getCpf().isEmpty()) {
                throw new InvalidDataException("CPF é obrigatório para brasileiros.");
            }
            validarCPF(ciclista.getCpf());
        } else if (ciclista.getNacionalidade() == Nacionalidade.ESTRANGEIRO) {
            if (ciclista.getPassaporte() == null) {
                throw new InvalidDataException("Passaporte é obrigatório para estrangeiros.");
            }
        }
    }


    public Optional<Ciclista> obterCiclista(int idCiclista) {
        return Optional.ofNullable(ciclistaRepository.findById(idCiclista)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista)));
    }

    private void validarSenhasIdenticas(String senha, String confirmacaoSenha) {
        if (senha == null || !senha.equals(confirmacaoSenha)) {
            throw new InvalidDataException("As senhas não são idênticas.");
        }
    }

    public Ciclista alterarCiclista(int idCiclista, NovoCiclistaDTO novoCiclista) throws BadRequestException {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(
                () -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista)
        );
        validarCiclistaParaAlterar(ciclista);
        validarSenhasIdenticas(ciclista.getSenha(), ciclista.getConfirmacaoSenha());
        ciclista.setId(idCiclista);

        // atualizar ciclista com os dados do novoCiclista
        ciclista.setNome(novoCiclista.getNome());
        ciclista.setNascimento(novoCiclista.getNascimento());
        ciclista.setNacionalidade(novoCiclista.getNacionalidade());
        ciclista.setEmail(novoCiclista.getEmail());
        ciclista.setUrlFotoDocumento(novoCiclista.getUrlFotoDocumento());
        ciclista.setCpf(novoCiclista.getCpf());
        ciclista.setPassaporte(passaporteMapper.toEntity(novoCiclista.getPassaporte()));

        ciclistaRepository.save(ciclista);
        return ciclista;
    }

    public Ciclista ativarCiclista(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(
                () -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista)
        );
        ciclista.setStatus(StatusCiclista.ATIVO);
        return ciclistaRepository.save(ciclista);
    }

    public boolean permiteAluguel(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        return ciclista.getStatus() == StatusCiclista.ATIVO && !aluguelRepository.existsByCiclistaAndHoraFimIsNull(ciclista.getId());
    }

    public Optional<Bicicleta> obterBicicletaAlugada(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista));
        Integer bicicletaId = aluguelRepository.findByCiclistaAndHoraFimIsNull(ciclista.getId()).map(Aluguel::getBicicleta).orElse(null);
        if (bicicletaId != null) {
            return Optional.of(bicicletaService.getBicicletaById(idCiclista));
        }
        return Optional.empty();
    }

    public boolean existeEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !email.matches(emailRegex)) {
            throw new InvalidDataException("Dados inválidos.");
        }
        return ciclistaRepository.existsByEmail(email);
    }

    private void validarCamposObrigatorios(NovoCiclistaDTO novoCiclistaDTO) {
        if (novoCiclistaDTO.getNome() == null || novoCiclistaDTO.getNome().isEmpty() ||
                novoCiclistaDTO.getEmail() == null || novoCiclistaDTO.getEmail().isEmpty() ||
                novoCiclistaDTO.getNascimento() == null || novoCiclistaDTO.getNascimento().isEmpty() ||
                novoCiclistaDTO.getNacionalidade() == null ||
                (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.BRASILEIRO) &&
                        (novoCiclistaDTO.getCpf() == null || novoCiclistaDTO.getCpf().isEmpty())) ||
                (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.ESTRANGEIRO) &&
                        novoCiclistaDTO.getPassaporte() == null)) {
            throw new BadRequestException("Todos os campos são obrigatórios.");
        }

        if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.BRASILEIRO)) {
            validarCPF(novoCiclistaDTO.getCpf());
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
            throw new InvalidDataException("Email já cadastrado.");
        }
    }
}
