package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.BicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BicicletaService {

    private static final String TRANCA_NAO_ENCONTRADA = "Tranca não encontrada";
    private static final String FUNCIONARIO_INVALIDO = "Funcionário inválido para esta operação";
    private static final String BICICLETA_NAO_ENCONTRADA = "Bicicleta não encontrada";
    private static final String EMAIL_ENVIADO_PARA_O_REPARADOR = "Email enviado para o reparador";
    private static final String STATUS_DA_BICICLETA_INVALIDO = "Status da bicicleta inválido";

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
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new IllegalArgumentException(BICICLETA_NAO_ENCONTRADA));

        if (bicicleta.getStatus() != StatusBicicleta.NOVA && bicicleta.getStatus() != StatusBicicleta.EM_REPARO) {
            throw new IllegalArgumentException(STATUS_DA_BICICLETA_INVALIDO);
        }

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException(TRANCA_NAO_ENCONTRADA);
        }

        if (bicicleta.getStatus() == StatusBicicleta.EM_REPARO && !isFuncionarioValido(dto.getIdFuncionario(), bicicleta.getId())) {
            throw new IllegalArgumentException(FUNCIONARIO_INVALIDO);
        }

        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);

        System.out.println(EMAIL_ENVIADO_PARA_O_REPARADOR);
    }

    public void retirarDaRede(RetirarBicicletaDaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new IllegalArgumentException(BICICLETA_NAO_ENCONTRADA));

        if (bicicleta.getStatus() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new IllegalArgumentException(STATUS_DA_BICICLETA_INVALIDO);
        }

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new IllegalArgumentException(TRANCA_NAO_ENCONTRADA);
        }

        bicicleta.setStatus(StatusBicicleta.EM_REPARO);
        //bicicleta.setDataRetiradaTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);

        System.out.println(EMAIL_ENVIADO_PARA_O_REPARADOR);
    }

    public boolean isFuncionarioValido(Long idFuncionario, Long idFuncionarioReparador) {
        // Chamar endpoint de validação de funcionário
        return true; // Substituir com a validação real
    }

}
