package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.CiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
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
        validarCamposObrigatorios(ciclista);
        validarSenha(ciclista.getSenha(), ciclista.getConfirmacaoSenha());
        validarEmail(novoCiclistaDTO.getEmail());

        ciclista = ciclistaRepository.save(ciclista);
        enviarEmailConfirmacao(ciclista.getEmail());
        return ciclistaMapper.toDto(ciclista);
    }

    public Optional<CiclistaDTO> obterCiclista(Long idCiclista) {
        return ciclistaRepository.findById(idCiclista)
                .map(ciclistaMapper::toDto);
    }

    public CiclistaDTO alterarCiclista(Long idCiclista, CiclistaDTO ciclistaDTO) {
        validarCamposObrigatorios(ciclistaDTO);
        validarSenha(ciclistaDTO.getSenha(), ciclistaDTO.getConfirmacaoSenha());

        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        ciclista.setNome(ciclistaDTO.getNome());
        ciclista.setEmail(ciclistaDTO.getEmail());
        ciclista.setCpf(ciclistaDTO.getCpf());
        ciclista.setDataNascimento(ciclistaDTO.getDataNascimento());
        ciclista.setNacionalidade(ciclistaDTO.getNacionalidade());
        ciclista.setStatusCiclista(ciclistaDTO.getStatusCiclista());
        ciclista = ciclistaRepository.save(ciclista);
        return ciclistaMapper.toDto(ciclista);
    }

    public void ativarCiclista(Long idCiclista) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista).orElseThrow();
        ciclista.setStatusCiclista(StatusCiclista.ATIVO);
        ciclistaRepository.save(ciclista);
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
        if (novoCiclistaDTO.getNacionalidade().equals("Brasileiro")) {
            if (novoCiclistaDTO.getCpf() == null || novoCiclistaDTO.getCpf().isEmpty()) {
                throw new IllegalArgumentException("CPF é obrigatório para brasileiros.");
            }
        } else {
            if (novoCiclistaDTO.getPassaporte() == null || novoCiclistaDTO.getPassaporte().isEmpty() ||
                    novoCiclistaDTO.getNacionalidade() == null || novoCiclistaDTO.getNacionalidade().isEmpty()) {
                throw new IllegalArgumentException("Passaporte e país são obrigatórios para estrangeiros.");
            }
        }
    }

    private void validarCamposObrigatorios(CiclistaDTO ciclistaDTO) {
        if (ciclistaDTO.getNacionalidade().equals("Brasileiro")) {
            if (ciclistaDTO.getCpf() == null || ciclistaDTO.getCpf().isEmpty()) {
                throw new IllegalArgumentException("CPF é obrigatório para brasileiros.");
            }
        } else {
            if (ciclistaDTO.getPassaporte() == null || ciclistaDTO.getPassaporte().isEmpty() ||
                    ciclistaDTO.getPais() == null || ciclistaDTO.getPais().isEmpty()) {
                throw new IllegalArgumentException("Passaporte e país são obrigatórios para estrangeiros.");
            }
        }
    }

    private void validarSenha(String senha, String confirmacaoSenha) {
        if (senha == null || confirmacaoSenha == null || !senha.equals(confirmacaoSenha)) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
    }

    private void validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null || !pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido.");
        }
    }

    private void enviarEmailConfirmacao(String email) {
        // Lógica para enviar email
        System.out.println("Email de confirmação enviado para: " + email);
    }
}
