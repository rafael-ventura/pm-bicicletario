package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.AdministradoraCCService;
import com.example.bicicletario.bicicletario.application.external.BicicletaService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CiclistaService {

    @Autowired
    private CiclistaRepository ciclistaRepository;
    @Autowired
    private CiclistaMapper ciclistaMapper;
    @Autowired
    private AluguelRepository aluguelRepository;
    @Autowired
    private AdministradoraCCService administradoraCCService; // Serviço para validação do cartão de crédito
    @Autowired
    private BicicletaService bicicletaService; // Serviço para validação do cartão de crédito
    @Autowired
    private CartaoDeCreditoService cartaoDeCreditoService;
    
    public Ciclista cadastrarCiclista(NovoCiclistaRequestDTO request) throws BadRequestException {
        NovoCiclistaDTO novoCiclistaDTO = request.getCiclista();
        NovoCartaoDeCreditoDTO meioDePagamentoDTO = request.getMeioDePagamento();

        validarCiclista(request);

        // Validação do cartão de crédito junto à Administradora CC
        administradoraCCService.validarCartao(meioDePagamentoDTO, true);

        Ciclista ciclista = ciclistaMapper.toEntity(novoCiclistaDTO);
        ciclistaRepository.save(ciclista);
        enviarEmailConfirmacao(ciclista.getEmail());
        return ciclista;
    }

    private void validarCiclista(NovoCiclistaRequestDTO novoCiclistaRequest) {
        validarCamposObrigatorios(novoCiclistaRequest.getCiclista());
        validarSenha(novoCiclistaRequest.getCiclista().getSenha(), novoCiclistaRequest.getCiclista().getConfirmacaoSenha());
        validarEmail(novoCiclistaRequest.getCiclista().getEmail());
        validarCartaoDeCredito(novoCiclistaRequest.getMeioDePagamento());
    }

    private void validarCartaoDeCredito(NovoCartaoDeCreditoDTO cartaoDeCredito) {
        cartaoDeCreditoService.validarCartaoDeCredito(cartaoDeCredito);
    }

    public Optional<Ciclista> obterCiclista(int idCiclista) {
        return ciclistaRepository.findById(idCiclista);
    }

    public Ciclista alterarCiclista(int idCiclista, NovoCiclistaDTO novoCiclistaDTO) throws BadRequestException {
        validarCamposObrigatorios(novoCiclistaDTO);
        validarSenha(novoCiclistaDTO.getSenha(), novoCiclistaDTO.getConfirmacaoSenha());
        validarEmail(novoCiclistaDTO.getEmail());
        if (!ciclistaRepository.existsById(idCiclista)) {
            throw new InvalidDataException("Ciclista não encontrado com o ID: " + idCiclista);
        }
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
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow(() -> new ResourceNotFoundException("Ciclista não encontrado com o ID: " + idCiclista));
        Integer bicicletaId = aluguelRepository.findByCiclistaAndHoraFimIsNull(ciclista.getId()).map(Aluguel::getBicicleta).orElse(null);
        if (bicicletaId != null) {
            return Optional.of(bicicletaService.getBicicleta(bicicletaId));
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
                novoCiclistaDTO.getNacionalidade() == null) {
            throw new BadRequestException("Todos os campos são obrigatórios.");
        }

        if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.BRASILEIRO)) {
            if (novoCiclistaDTO.getCpf() == null || novoCiclistaDTO.getCpf().isEmpty()) {
                throw new BadRequestException("CPF é obrigatório para brasileiros.");
            }
            validarCPF(novoCiclistaDTO.getCpf());
        } else if (novoCiclistaDTO.getNacionalidade().equals(Nacionalidade.ESTRANGEIRO)) {
            if (novoCiclistaDTO.getPassaporte() == null) {
                throw new BadRequestException("Passaporte e País são obrigatórios para estrangeiros.");
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
            throw new InvalidDataException("Email já cadastrado.");
        }
    }

    private void enviarEmailConfirmacao(String email) {
        System.out.println("Email de confirmação enviado para: " + email);
        // Lógica para enviar email
    }
}
