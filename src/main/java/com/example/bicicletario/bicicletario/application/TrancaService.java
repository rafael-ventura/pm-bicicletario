package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.dto.TrancaDTO;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrancaService {

    private final TrancaRepository trancaRepository;
    private final TrancaMapper trancaMapper;

    @Autowired
    public TrancaService(TrancaRepository trancaRepository, TrancaMapper trancaMapper) {
        this.trancaRepository = trancaRepository;
        this.trancaMapper = trancaMapper;
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Optional<Tranca> trancaOpt = trancaRepository.findById(dto.getIdTranca());
        if (trancaOpt.isEmpty() || !trancaOpt.get().getStatus().equals(StatusTranca.LIVRE)) {
            throw new IllegalArgumentException("Tranca não está disponível");
        }

        Tranca trancaDb = trancaOpt.get();
        trancaDb.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(trancaDb);
    }

    public void retirarDaRede(RetirarTrancaDaRedeDTO dto) {
        Optional<Tranca> trancaOpt = trancaRepository.findById(dto.getIdTranca());
        if (trancaOpt.isEmpty()) {
            throw new IllegalArgumentException("Número da tranca inválido");
        }

        Tranca tranca = trancaOpt.get();

        if (trancaTemBicicleta(tranca)) {
            throw new IllegalArgumentException("Tranca está com bicicleta presa");
        }

        if (dto.getStatusAcaoReparador() == null) {
            throw new IllegalArgumentException("Status de ação do reparador inválido");
        }

        if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.EM_REPARO)) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.APOSENTADA)) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        } else {
            throw new IllegalArgumentException("Status de ação do reparador inválido");
        }

        trancaRepository.save(tranca);

        try {
            System.out.println("Email enviado com sucesso");
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro no envio do email");
        }
    }

    private boolean trancaTemBicicleta(Tranca tranca) {
        return tranca.getStatus().equals(StatusTranca.OCUPADA);
    }

    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    public Tranca cadastrarTranca(TrancaDTO tranca) {
        return trancaRepository.save(trancaMapper.toTranca(tranca));
    }

    public Tranca obterTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
    }

    public Tranca editarTranca(Long idTranca, TrancaDTO tranca) {
        Tranca existente = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        existente.setStatus(StatusTranca.valueOf(tranca.getStatus()));
        return trancaRepository.save(existente);
    }

    public void removerTranca(Long idTranca) {
        trancaRepository.deleteById(idTranca);
    }

    public Tranca obterBicicletaNaTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
    }

    public void trancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);
    }

    public void destrancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);
    }

    public void alterarStatusTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        tranca.setStatus(StatusTranca.valueOf(acao.toUpperCase()));
        trancaRepository.save(tranca);
    }
}
