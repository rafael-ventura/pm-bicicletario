package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.dto.IntegrarNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarDaRedeDTO;
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

    public void integrarNaRede(IntegrarNaRedeDTO dto) {
        Optional<Tranca> trancaOpt = trancaRepository.findById(dto.getIdTranca());
        if (trancaOpt.isEmpty() || !trancaOpt.get().getStatus().equals("DISPONIVEL")) {
            throw new IllegalArgumentException("Tranca não está disponível");
        }

        Tranca trancaDb = trancaOpt.get();
        trancaDb.setStatus("DISPONIVEL");
        trancaRepository.save(trancaDb);
    }

    public void retirarDaRede(RetirarDaRedeDTO dto) {
        Optional<Tranca> trancaOpt = trancaRepository.findById(dto.getIdTranca());
        if (trancaOpt.isEmpty()) {
            throw new IllegalArgumentException("Tranca não encontrada");
        }

        Tranca trancaDb = trancaOpt.get();
        trancaDb.setStatus(dto.getStatusAcaoReparador());
        trancaRepository.save(trancaDb);
    }

    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    public Tranca cadastrarTranca(Tranca tranca) {
        return trancaRepository.save(tranca);
    }

    public Tranca obterTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
    }

    public Tranca editarTranca(Long idTranca, Tranca tranca) {
        Tranca existente = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        existente.setStatus(tranca.getStatus());
        return trancaRepository.save(existente);
    }

    public void removerTranca(Long idTranca) {
        trancaRepository.deleteById(idTranca);
    }

    public Tranca obterBicicletaNaTranca(Long idTranca) {
        // Implementar lógica para obter bicicleta na tranca, se aplicável
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
    }

    public void trancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        tranca.setStatus("TRANCADA");
        trancaRepository.save(tranca);
        // Implementar lógica para associar bicicleta, se aplicável
    }

    public void destrancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        tranca.setStatus("DESTRANCADA");
        trancaRepository.save(tranca);
        // Implementar lógica para desassociar bicicleta, se aplicável
    }

    public void alterarStatusTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException("Tranca não encontrada"));
        tranca.setStatus(acao);
        trancaRepository.save(tranca);
    }
}
