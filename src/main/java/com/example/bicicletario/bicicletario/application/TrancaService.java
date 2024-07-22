package com.example.bicicletario.bicicletario.application;

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
    private final BicicletaRepository bicicletaRepository;
    private final TrancaMapper trancaMapper;
    private final EmailService emailService;
    private final FuncionarioService funcionarioService;

    public TrancaService(TrancaRepository trancaRepository, BicicletaRepository bicicletaRepository, TrancaMapper trancaMapper, EmailService emailService, FuncionarioService funcionarioService) {
        this.trancaRepository = trancaRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.trancaMapper = trancaMapper;
        this.emailService = emailService;
        this.funcionarioService = funcionarioService;
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        // [E1] Verificar se a tranca existe
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        // [R3] Verificar o status da tranca
        if (tranca.getStatus() != StatusTranca.NOVA && tranca.getStatus() != StatusTranca.EM_REPARO) {
            throw new IllegalArgumentException("Status da tranca inválido");
        }

        // [R3] Verificar se o funcionário que está devolvendo a tranca é o mesmo que retirou para reparo
        if (tranca.getStatus() == StatusTranca.EM_REPARO && !funcionarioService.isFuncionarioValido(dto.getIdFuncionario())) {
            throw new IllegalArgumentException(Constantes.FUNCIONARIO_INVALIDO);
        }

        // [R1] Registrar data/hora da inserção no totem, a matrícula do reparador e o número da tranca
        tranca.setStatus(StatusTranca.LIVRE);
        tranca.setDataInsercaoTotem(LocalDateTime.now().toString());
        trancaRepository.save(tranca);

        // [R2] Enviar email para o reparador
        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            // [E2] Tratar erro no envio do email
            throw new IllegalArgumentException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public void retirarDaRede(RetirarTrancaDaRedeDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (trancaTemBicicleta(tranca)) {
            throw new IllegalArgumentException(Constantes.TRANCA_PRENCHIDA);
        }

        if (dto.getStatusAcaoReparador() == null) {
            throw new IllegalArgumentException(Constantes.STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }

        if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.EM_REPARO)) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.APOSENTADA)) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        } else {
            throw new IllegalArgumentException(Constantes.STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }

        trancaRepository.save(tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new IllegalArgumentException(Constantes.ERROR_ENVIAR_EMAIL);
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
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    public Tranca editarTranca(Long idTranca, NovaTrancaDTO trancaDTO) {
        Tranca existente = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));
        existente.setLocalizacao(trancaDTO.getLocalizacao());
        return trancaRepository.save(existente);
    }

    public void removerTranca(Long idTranca) {
        trancaRepository.deleteById(idTranca);
    }

    public Tranca obterBicicletaNaTranca(Long idTranca) {
        return trancaRepository.findById(idTranca).orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    public void trancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException("Tranca não está livre");
        }

        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada"));

        tranca.setStatus(StatusTranca.OCUPADA);
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void destrancarTranca(Long idTranca, Long bicicletaId) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new IllegalArgumentException("Tranca não está ocupada");
        }

        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta não encontrada"));

        tranca.setStatus(StatusTranca.LIVRE);
        bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void alterarStatusTranca(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        try {
            StatusTranca novoStatus = StatusTranca.valueOf(acao.toUpperCase());
            tranca.setStatus(novoStatus);
            trancaRepository.save(tranca);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de tranca inválido");
        }
    }
}
