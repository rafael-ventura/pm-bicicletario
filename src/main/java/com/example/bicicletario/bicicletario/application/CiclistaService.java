package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.BicicletaService;
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
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CiclistaService {

    private final CiclistaRepository ciclistaRepository;
    private final CiclistaMapper ciclistaMapper;
    private final AluguelRepository aluguelRepository;
    private final BicicletaService bicicletaService;
    private final CartaoDeCreditoService cartaoDeCreditoService;

    public CiclistaService(CiclistaRepository ciclistaRepository, CiclistaMapper ciclistaMapper, AluguelRepository aluguelRepository, BicicletaService bicicletaService, CartaoDeCreditoService cartaoDeCreditoService) {
        this.ciclistaRepository = ciclistaRepository;
        this.ciclistaMapper = ciclistaMapper;
        this.aluguelRepository = aluguelRepository;
        this.bicicletaService = bicicletaService;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
    }

    public Ciclista cadastrarCiclista(NovoCiclistaRequestDTO request) throws BadRequestException {
        validarCiclista(request);

        NovoCiclistaDTO novoCiclistaDTO = request.getCiclista();
        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);

        cartaoDeCreditoService.save(request.getMeioDePagamento(), ciclista.getId());

        ciclistaRepository.save(ciclista);
        enviarEmailConfirmacao(ciclista.getEmail());
        return ciclista;
    }

    private void validarCiclista(NovoCiclistaRequestDTO novoCiclistaRequest) {
        validarCamposObrigatorios(novoCiclistaRequest.getCiclista());
        validarEmail(novoCiclistaRequest.getCiclista().getEmail());
        cartaoDeCreditoService.validarCartaoDeCredito(novoCiclistaRequest.getMeioDePagamento());
    }

    private void validarCiclista(NovoCiclistaDTO novoCiclistaDTO) {
        validarCamposObrigatorios(novoCiclistaDTO);
        validarEmail(novoCiclistaDTO.getEmail());
    }

    public Optional<Ciclista> obterCiclista(int idCiclista) {
        return Optional.ofNullable(ciclistaRepository.findById(idCiclista)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista)));
    }

    public Ciclista alterarCiclista(int idCiclista, NovoCiclistaDTO novoCiclistaDTO) throws BadRequestException {
        if (!ciclistaRepository.existsById(idCiclista)) {
            throw new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista);
        }
        validarCiclista(novoCiclistaDTO);
        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        ciclista.setId(idCiclista);

        ciclistaRepository.save(ciclista);
        return ciclista;
    }

    public Ciclista ativarCiclista(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        ciclista.setStatusCiclista(StatusCiclista.ATIVO);
        return ciclistaRepository.save(ciclista);
    }

    public boolean permiteAluguel(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        return ciclista.getStatusCiclista() == StatusCiclista.ATIVO && !aluguelRepository.existsByCiclistaAndHoraFimIsNull(ciclista.getId());
    }

    public Optional<Bicicleta> obterBicicletaAlugada(int idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new ResourceNotFoundException(Constants.CICLISTA_NAO_ENCONTRADO + idCiclista));
        Integer bicicletaId = aluguelRepository.findByCiclistaAndHoraFimIsNull(ciclista.getId()).map(Aluguel::getBicicleta).orElse(null);
        if (bicicletaId != null) {
            return Optional.of(bicicletaService.getBicicleta());
        }
        return Optional.empty();
    }

    public boolean existeEmail(String email) {
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

    private void enviarEmailConfirmacao(String email) {
        Logger.getLogger("Email de confirmação enviado para: " + email);
        // Lógica para enviar email
    }
}
