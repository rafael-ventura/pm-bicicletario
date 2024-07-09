package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.TrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrancaService {

    private static final String TRANCA_NAO_ENCONTRADA = "Tranca não encontrada";
    private static final String TRANCA_PRENCHIDA = "Tranca está com bicicleta presa";
    private static final String STATUS_DE_ACAO_REPARADOR_INVALIDO = "Status de ação do reparador inválido";
    private static final String EMAIL_ENVIADO_PARA_O_REPARADOR = "Email enviado para o reparador";

    private final TrancaRepository trancaRepository;
    private final TrancaMapper trancaMapper;
    private final BicicletaRepository bicicletaRepository;

    @Autowired
    public TrancaService(TrancaRepository trancaRepository, TrancaMapper trancaMapper, BicicletaRepository bicicletaRepository) {
        this.trancaRepository = trancaRepository;
        this.trancaMapper = trancaMapper;
        this.bicicletaRepository = bicicletaRepository;
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException("Tranca não está disponível");
        }

        tranca.setStatus(StatusTranca.OCUPADA); // Atualizado para ocupada ao integrar bicicleta
        trancaRepository.save(tranca);

        System.out.println(EMAIL_ENVIADO_PARA_O_REPARADOR); // Simula envio de email
    }

    public void retirarDaRede(RetirarTrancaDaRedeDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        if (trancaTemBicicleta(tranca)) {
            throw new IllegalArgumentException(TRANCA_PRENCHIDA);
        }

        if (dto.getStatusAcaoReparador() == null) {
            throw new IllegalArgumentException(STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }

        if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.EM_REPARO)) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.APOSENTADA)) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        } else {
            throw new IllegalArgumentException(STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }

        trancaRepository.save(tranca);

        System.out.println(EMAIL_ENVIADO_PARA_O_REPARADOR); // Simula envio de email
    }

    private boolean trancaTemBicicleta(Tranca tranca) {
        return tranca.getStatus().equals(StatusTranca.OCUPADA);
    }

    public List<Tranca> listarTrancas() {
        return trancaRepository.findAll();
    }

    public Tranca cadastrarTranca(TrancaDTO trancaDTO) {
        return trancaRepository.save(trancaMapper.toTranca(trancaDTO));
    }

    public Tranca obterTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));
    }

    public Tranca editarTranca(Long idTranca, TrancaDTO trancaDTO) {
        Tranca existente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));
        existente.setStatus(StatusTranca.valueOf(trancaDTO.getStatus()));
        return trancaRepository.save(existente);
    }

    public void removerTranca(Long idTranca) {
        trancaRepository.deleteById(idTranca);
    }

    public Tranca obterBicicletaNaTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));
    }

    public void trancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException("Tranca não está livre");
        }

        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada"));

        tranca.setStatus(StatusTranca.OCUPADA);
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void destrancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new IllegalArgumentException("Tranca não está ocupada");
        }

        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada"));

        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setStatus(StatusBicicleta.EM_USO);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void alterarStatusTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de tranca inválido");
        }
    }
}
