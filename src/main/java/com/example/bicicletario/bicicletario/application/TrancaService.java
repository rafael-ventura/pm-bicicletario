package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrancaService {

    private final TrancaRepository trancaRepository;
    private final TrancaMapper trancaMapper;
    private final EmailService emailService;
    private final FuncionarioService funcionarioService;
    private final BicicletaRepository bicicletaRepository;

    public TrancaService(TrancaRepository trancaRepository, TrancaMapper trancaMapper, EmailService emailService, FuncionarioService funcionarioService, BicicletaRepository bicicletaRepository) {
        this.trancaRepository = trancaRepository;
        this.trancaMapper = trancaMapper;
        this.emailService = emailService;
        this.funcionarioService = funcionarioService;
        this.bicicletaRepository = bicicletaRepository;
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.NOVA && tranca.getStatus() != StatusTranca.EM_REPARO) {
            throw new InvalidDataException("Status da tranca inválido");
        }

        if (tranca.getStatus() == StatusTranca.EM_REPARO && !funcionarioService.isFuncionarioValido(dto.getIdFuncionario())) {
            throw new InvalidDataException("Funcionário inválido");
        }

        tranca.setStatus(StatusTranca.LIVRE);
        tranca.setDataInsercaoTotem(LocalDateTime.now().toString());
        trancaRepository.save(tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public void retirarDaRede(RetirarTrancaDaRedeDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (trancaTemBicicleta(tranca)) {
            throw new InvalidDataException(Constantes.TRANCA_PRENCHIDA);
        }

        if (dto.getStatusAcaoReparador() == null) {
            throw new InvalidDataException(Constantes.STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }

        if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.EM_REPARO)) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.APOSENTADA)) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        } else {
            throw new InvalidDataException(Constantes.STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }

        trancaRepository.save(tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    private boolean trancaTemBicicleta(Tranca tranca) {
        return tranca.getStatus().equals(StatusTranca.OCUPADA);
    }

    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    public Tranca cadastrarTranca(NovaTrancaDTO trancaDTO) {
        Tranca tranca = trancaMapper.toEntity(trancaDTO);
        return trancaRepository.save(tranca);
    }

    public Tranca obterTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    public Tranca editarTranca(Long idTranca, NovaTrancaDTO trancaDTO) {
        Tranca existente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
        existente.setLocalizacao(trancaDTO.getLocalizacao());
        return trancaRepository.save(existente);
    }

    public void removerTranca(Long idTranca) {
        trancaRepository.deleteById(idTranca);
    }

    public Tranca obterBicicletaNaTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    public void trancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new InvalidDataException("Tranca não está livre");
        }

        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta não encontrada"));

        tranca.setStatus(StatusTranca.OCUPADA);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void destrancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new InvalidDataException("Tranca não está ocupada");
        }

        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta não encontrada"));

        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void alterarStatusTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Status de tranca inválido");
        }
    }
}
