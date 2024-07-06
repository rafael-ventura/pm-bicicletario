package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final TrancaRepository trancaRepository;
    private final BicicletaMapper bicicletaMapper;

    public BicicletaService(BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository, BicicletaMapper bicicletaMapper) {
        this.bicicletaRepository = bicicletaRepository;
        this.trancaRepository = trancaRepository;
        this.bicicletaMapper = bicicletaMapper;
    }

    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    public BicicletaDTO criarBicicleta(BicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = bicicletaMapper.toBicicleta(bicicletaDTO);
        bicicleta = bicicletaRepository.save(bicicleta);
        return bicicletaMapper.toDto(bicicleta);
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Optional<Bicicleta> bicicletaOpt = bicicletaRepository.findById(dto.getIdBicicleta());
        if (bicicletaOpt.isEmpty()) {
            throw new IllegalArgumentException("Número da bicicleta inválido");
        }

        Bicicleta bicicleta = bicicletaOpt.get();

        if (bicicleta.getStatus() != StatusBicicleta.NOVA && bicicleta.getStatus() != StatusBicicleta.EM_REPARO) {
            throw new IllegalArgumentException("Status da bicicleta inválido");
        }

        Optional<Tranca> trancaOpt = trancaRepository.findById(dto.getIdTranca());
        if (trancaOpt.isEmpty() || trancaOpt.get().getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException("Tranca não está disponível");
        }

        Tranca tranca = trancaOpt.get();

        if (bicicleta.getStatus() == StatusBicicleta.EM_REPARO && !isFuncionarioValido(dto.getIdFuncionario(), bicicleta.getId())) {
            throw new IllegalArgumentException("Funcionário inválido para esta operação");
        }

        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);

        try {
            enviarEmailReparador(bicicleta, dto.getIdFuncionario());
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro no envio do email");
        }
    }

    public void retirarDaRede(RetirarBicicletaDaRedeDTO dto) {
        Optional<Bicicleta> bicicletaOpt = bicicletaRepository.findById(dto.getIdBicicleta());
        if (bicicletaOpt.isEmpty()) {
            throw new IllegalArgumentException("Número da bicicleta inválido");
        }

        Bicicleta bicicleta = bicicletaOpt.get();

        if (bicicleta.getStatus() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new IllegalArgumentException("Status da bicicleta inválido");
        }

        Optional<Tranca> trancaOpt = trancaRepository.findById(dto.getIdTranca());
        if (trancaOpt.isEmpty() || trancaOpt.get().getStatus() != StatusTranca.OCUPADA) {
            throw new IllegalArgumentException("Tranca não está ocupada");
        }

        Tranca tranca = trancaOpt.get();

        bicicleta.setStatus(StatusBicicleta.EM_REPARO);
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);

        try {
            enviarEmailReparador(bicicleta, dto.getIdFuncionario());
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro no envio do email");
        }
    }

    private boolean isFuncionarioValido(Long idFuncionario, Long idFuncionarioReparador) {
        // Implementar a lógica correta de validação do funcionário
        // Substituir com a lógica real de validação
        return true; // Substituir com a validação real
    }

    private void enviarEmailReparador(Bicicleta bicicleta, Long idFuncionario) throws Exception {
        // Lógica para enviar email ao reparador
        // Substituir com a lógica real de envio de email
    }
}
