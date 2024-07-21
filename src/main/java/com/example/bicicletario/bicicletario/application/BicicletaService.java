package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public Bicicleta criarBicicleta(NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = bicicletaMapper.toEntity(bicicletaDTO);
        bicicleta = bicicletaRepository.save(bicicleta);
        return bicicleta;
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        if (bicicleta.getStatus() != StatusBicicleta.NOVA && bicicleta.getStatus() != StatusBicicleta.EM_REPARO) {
            throw new IllegalArgumentException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA);
        }

        if (bicicleta.getStatus() == StatusBicicleta.EM_REPARO && !isFuncionarioValido(dto.getIdFuncionario(), bicicleta.getId())) {
            throw new IllegalArgumentException(Constantes.FUNCIONARIO_INVALIDO);
        }

        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);

        System.out.println(Constantes.EMAIL_ENVIADO_PARA_O_REPARADOR);
    }

    public void retirarDaRede(RetirarBicicletaDaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        if (bicicleta.getStatus() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new IllegalArgumentException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA);
        }

        bicicleta.setStatus(StatusBicicleta.EM_REPARO);
        //bicicleta.setDataRetiradaTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);

        System.out.println(Constantes.EMAIL_ENVIADO_PARA_O_REPARADOR);
    }

    public boolean isFuncionarioValido(Long idFuncionario, Long idFuncionarioReparador) {
        // Chamar endpoint de validação de funcionário
        return true; // Substituir com a validação real
    }

    public Bicicleta obterBicicleta(Long idBicicleta) {
        return bicicletaRepository.findById(idBicicleta
        ).orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));
    }

    public void removerBicicleta(Long idBicicleta) {
        bicicletaRepository.deleteById(idBicicleta);
    }

    public Bicicleta editarBicicleta(Long idBicicleta, NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        bicicleta.setMarca(bicicletaDTO.getMarca());
        bicicleta.setModelo(bicicletaDTO.getModelo());
        bicicleta.setAno(bicicletaDTO.getAno());
        bicicleta.setNumero(bicicletaDTO.getNumero());
        bicicleta.setStatus(bicicletaDTO.getStatus());

        bicicletaRepository.save(bicicleta);

        return bicicleta;
    }

    public Bicicleta alterarStatusBicicleta(Long idBicicleta, String acao) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        if (acao.equals("disponibilizar")) {
            bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        } else if (acao.equals("reparar")) {
            bicicleta.setStatus(StatusBicicleta.REPARO_SOLICITADO);
        } else {
            throw new IllegalArgumentException(Constantes.DADOS_INVALIDOS);
        }

        bicicletaRepository.save(bicicleta);

        return bicicleta;
    }

}
