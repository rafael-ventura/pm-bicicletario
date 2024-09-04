package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.application.external.EmailService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Constants;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaRequestDTO;
import com.example.bicicletario.bicicletario.domain.enums.Nacionalidade;
import com.example.bicicletario.bicicletario.domain.enums.StatusCiclista;
import com.example.bicicletario.bicicletario.domain.mapper.CiclistaMapper;
import com.example.bicicletario.bicicletario.domain.mapper.PassaporteMapper;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import com.example.bicicletario.bicicletario.exception.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
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
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

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

        // 1. Validação do email
        // 2. Validação dos dados pessoais
        validarCiclistaDto(request);

        // 3. Validação do cartão de crédito
        cartaoDeCreditoService.validarCartaoDeCredito(request.getMeioDePagamento());

        // 4. Mapeamento do DTO para a entidade Ciclista
        Ciclista ciclista = request.getCiclista();
        ciclista.setStatus(StatusCiclista.AGUARDANDO_CONFIRMACAO); // Define status inicial como aguardando confirmação
        ciclistaRepository.save(ciclista);

        // 5. Registro do meio de pagamento
        cartaoDeCreditoService.save(request.getMeioDePagamento(), ciclista.getId());

        // 6. Envio de email de confirmação
        try {
            emailService.enviarEmailConfirmacao(ciclista);
        } catch (Exception e) {
            logger.error("Erro ao enviar email de confirmação para {}", ciclista.getEmail(), e);
            throw new BadRequestException("Erro ao enviar email de confirmação. Por favor, tente novamente.");
        }

        logger.info("Ciclista cadastrado com sucesso!");
        return ciclista;
    }

    private void validarCiclistaDto(NovoCiclistaRequestDTO novoCiclistaRequest) {
        validarCamposObrigatorios(novoCiclistaRequest.getCiclista());
        validarEmail(novoCiclistaRequest.getCiclista().getEmail());
        cartaoDeCreditoService.validarCartaoDeCredito(novoCiclistaRequest.getMeioDePagamento());
    }

    private void validarCiclistaParaAlterar(Ciclista ciclista) {
        if (ciclista.getEmail() == null || !ciclista.getEmail().matches(EMAIL_REGEX)) {
                throw new InvalidDataException("Email inválido.");
        }

        if (ciclista.getNacionalidade() == Nacionalidade.BRASILEIRO) {
            if (ciclista.getCpf() == null || ciclista.getCpf().isEmpty()) {
                throw new InvalidDataException("CPF é obrigatório para brasileiros.");
            }
            validarCPF(ciclista.getCpf());
        }
        if (ciclista.getNacionalidade() == Nacionalidade.ESTRANGEIRO && ciclista.getPassaporte() == null) {
            throw new InvalidDataException("Passaporte é obrigatório para estrangeiros.");
        }
    }


    public Optional<Ciclista> obterCiclista(int idCiclista) {
        return Optional.ofNullable(ciclistaRepository.findById(idCiclista)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista)));
    }

    public Ciclista alterarCiclista(int idCiclista, NovoCiclistaDTO novoCiclista) throws BadRequestException {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(
                () -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista)
        );
        Ciclista novoCiclistaDomain = ciclistaMapper.toEntity(novoCiclista);
        validarCiclistaParaAlterar(novoCiclistaDomain);
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
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new InvalidDataException("Dados inválidos.");
        }
        return ciclistaRepository.existsByEmail(email);
    }

    public void validarCamposObrigatorios(Ciclista ciclista) {
        if (ciclista.getNome() == null || ciclista.getNome().isEmpty() ||
                ciclista.getEmail() == null || ciclista.getEmail().isEmpty() ||
                ciclista.getNascimento() == null || ciclista.getNascimento().isEmpty() ||
                ciclista.getNacionalidade() == null ||
                (ciclista.getNacionalidade().equals(Nacionalidade.BRASILEIRO) &&
                        (ciclista.getCpf() == null || ciclista.getCpf().isEmpty())) ||
                (ciclista.getNacionalidade().equals(Nacionalidade.ESTRANGEIRO) &&
                        ciclista.getPassaporte() == null)) {
            throw new BadRequestException("Todos os campos são obrigatórios.");
        }

        if (ciclista.getNacionalidade().equals(Nacionalidade.ESTRANGEIRO)) {
            if (ciclista.getPassaporte() != null) {
                if (ciclista.getPassaporte().getNumero() == null || ciclista.getPassaporte().getNumero().isEmpty() ||
                        ciclista.getPassaporte().getValidade().isEmpty() ||
                        validatePais(ciclista.getPassaporte().getPais()))
                {
                    throw new InvalidDataException("Número do passaporte é obrigatório.");
                }
                throw new InvalidDataException("Passaporte é obrigatório para estrangeiros.");
            }
        }

        if (ciclista.getNacionalidade().equals(Nacionalidade.BRASILEIRO)) {
            validarCPF(ciclista.getCpf());
        }

        if (ciclista.getSenha() == null || ciclista.getSenha().isEmpty()) {
            throw new InvalidDataException("Senha é obrigatória.");
        }
    }

    private boolean validatePais(String pais) {
        // formato do país: A-Z{2}
        return pais == null || pais.length() != 2 || !pais.matches("[A-Z]{2}");
    }

    private void validarCPF(String cpf) {
        String cpfRegex = "\\d{11}";
        if (cpf == null || !cpf.matches(cpfRegex)) {
            throw new InvalidDataException("CPF inválido. O CPF deve conter 11 dígitos e apenas números.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new InvalidDataException("Email inválido.");
        }
        if (ciclistaRepository.existsByEmail(email)) {
            throw new InvalidDataException("Email já cadastrado.");
        }
    }
}
