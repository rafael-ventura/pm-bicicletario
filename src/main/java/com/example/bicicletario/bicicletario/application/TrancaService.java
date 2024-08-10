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
        Tranca tranca = findTrancaByIdOrThrow(dto.getIdTranca());

        validarTrancaStatusParaIntegracao(tranca);

        if (tranca.getStatus() == StatusTranca.EM_REPARO) {
            validarFuncionarioParaReparo(dto.getIdFuncionario());
        }

        updateTrancaParaIntegracao(tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public void retirarDaRede(RetirarTrancaDaRedeDTO dto) {
        Tranca tranca = findTrancaByIdOrThrow(dto.getIdTranca());

        validarTrancaParaRemover(tranca, dto.getStatusAcaoReparador());

        updateTrancaParaRemover(tranca, dto.getStatusAcaoReparador());

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    public Tranca cadastrarTranca(NovaTrancaDTO trancaDTO) {
        Tranca tranca = trancaMapper.toEntity(trancaDTO);
        return trancaRepository.save(tranca);
    }

    public Tranca obterTranca(Long idTranca) {
        return findTrancaByIdOrThrow(idTranca);
    }

    public Tranca editarTranca(Long idTranca, NovaTrancaDTO trancaDTO) {
        Tranca existente = findTrancaByIdOrThrow(idTranca);
        existente.setLocalizacao(trancaDTO.getLocalizacao());
        return trancaRepository.save(existente);
    }

    public void removerTranca(Long idTranca) {
        trancaRepository.deleteById(idTranca);
    }

    public Tranca obterBicicletaNaTranca(Long idTranca) {
        return findTrancaByIdOrThrow(idTranca);
    }

    public void trancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = findTrancaByIdOrThrow(idTranca);
        Bicicleta bicicleta = findBicicletaByIdOrThrow(bicicletaId);

        validarTrancaStatusParaTrancar(tranca);

        updateTrancaEBicicletaParaTrancar(tranca, bicicleta);
    }

    public void destrancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = findTrancaByIdOrThrow(idTranca);
        Bicicleta bicicleta = findBicicletaByIdOrThrow(bicicletaId);

        validarTrancaStatusParaDestrancar(tranca);

        updateTrancaEBicicletaParaDestrancar(tranca, bicicleta);
    }

    public void alterarStatusTranca(Long idTranca, String acao) {
        Tranca tranca = findTrancaByIdOrThrow(idTranca);
        updateTrancaStatus(tranca, acao);
    }

    // Métodos auxiliares privados

    private Tranca findTrancaByIdOrThrow(Long idTranca) {
        return trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    private Bicicleta findBicicletaByIdOrThrow(Long idBicicleta) {
        return bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta não encontrada"));
    }

    private void validarTrancaStatusParaIntegracao(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.NOVA && tranca.getStatus() != StatusTranca.EM_REPARO) {
            throw new InvalidDataException("Status da tranca inválido");
        }
    }

    private void validarFuncionarioParaReparo(Long idFuncionario) {
        if (!funcionarioService.isFuncionarioValido(idFuncionario)) {
            throw new InvalidDataException("Funcionário inválido");
        }
    }

    private void updateTrancaParaIntegracao(Tranca tranca) {
        tranca.setStatus(StatusTranca.LIVRE);
        tranca.setDataInsercaoTotem(LocalDateTime.now().toString());
        trancaRepository.save(tranca);
    }

    private void validarTrancaParaRemover(Tranca tranca, StatusAcaoReparador statusAcaoReparador) {
        if (trancaTemBicicleta(tranca)) {
            throw new InvalidDataException(Constantes.TRANCA_PRENCHIDA);
        }
        if (statusAcaoReparador == null) {
            throw new InvalidDataException(Constantes.STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }
    }

    private void updateTrancaParaRemover(Tranca tranca, StatusAcaoReparador statusAcaoReparador) {
        if (statusAcaoReparador.equals(StatusAcaoReparador.EM_REPARO)) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (statusAcaoReparador.equals(StatusAcaoReparador.APOSENTADA)) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        }
        trancaRepository.save(tranca);
    }

    private void validarTrancaStatusParaTrancar(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new InvalidDataException("Tranca não está livre");
        }
    }

    private void updateTrancaEBicicletaParaTrancar(Tranca tranca, Bicicleta bicicleta) {
        tranca.setStatus(StatusTranca.OCUPADA);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    private void validarTrancaStatusParaDestrancar(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new InvalidDataException("Tranca não está ocupada");
        }
    }

    private void updateTrancaEBicicletaParaDestrancar(Tranca tranca, Bicicleta bicicleta) {
        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    private void updateTrancaStatus(Tranca tranca, String acao) {
        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Status de tranca inválido");
        }
    }

    private boolean trancaTemBicicleta(Tranca tranca) {
        return tranca.getStatus().equals(StatusTranca.OCUPADA);
    }
}
