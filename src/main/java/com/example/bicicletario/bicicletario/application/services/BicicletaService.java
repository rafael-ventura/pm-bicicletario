package com.example.bicicletario.bicicletario.application.services;

import com.example.bicicletario.bicicletario.application.exceptions.BadRequestException;
import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusBicicleta;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.mapper.BicicletaMapper;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final TrancaRepository trancaRepository;
    private final BicicletaMapper bicicletaMapper;
    private final EmailService emailService;
    private final FuncionarioService funcionarioService;


    public BicicletaService(BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository, BicicletaMapper bicicletaMapper, EmailService emailService, FuncionarioService funcionarioService) {
        this.bicicletaRepository = bicicletaRepository;
        this.trancaRepository = trancaRepository;
        this.bicicletaMapper = bicicletaMapper;
        this.emailService = emailService;
        this.funcionarioService = funcionarioService;

    }

    public void integrarBicicletaNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Bicicleta bicicleta = buscarBicicletaPorId(dto.getIdBicicleta());
        Tranca tranca = buscarTrancaPorId(dto.getIdTranca());

        validarStatusBicicletaParaIntegracao(bicicleta);
        validarStatusTrancaParaIntegracao(tranca);

        if (bicicleta.getStatusBicicleta() == StatusBicicleta.EM_REPARO) {
            validarFuncionarioParaReparo(dto.getIdFuncionario());
            if (bicicleta.getIdFuncionarioUltimaOperacao() != dto.getIdFuncionario()) {
                throw new InvalidDataException(Constantes.FUNCIONARIO_INVALIDO);
            }
        }

        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicleta.setIdFuncionarioUltimaOperacao(dto.getIdFuncionario());
        associarBicicletaETranca(bicicleta, tranca, StatusBicicleta.DISPONIVEL, StatusTranca.OCUPADA);

        emailService.enviarEmailParaBicicleta(dto.getIdFuncionario(), bicicleta, tranca, "Inclusão");
    }

    public void retirarBicicletaDaRede(RetirarBicicletaDaRedeDTO dto) {
        Bicicleta bicicleta = buscarBicicletaPorId(dto.getIdBicicleta());
        Tranca tranca = buscarTrancaPorId(dto.getIdTranca());

        validarStatusTrancaParaRemocao(tranca);
        validarStatusBicicletaParaRemocao(bicicleta, dto.getStatusAcaoReparador());

        bicicleta.setDataRemocaoTranca(LocalDateTime.now().toString());
        bicicleta.setIdFuncionarioUltimaOperacao(dto.getIdFuncionario());
        desassociarBicicletaETranca(bicicleta, tranca, definirStatusBicicleta(dto.getStatusAcaoReparador()), StatusTranca.LIVRE);

        emailService.enviarEmailParaBicicleta(dto.getIdFuncionario(), bicicleta, tranca, "Retirada");
    }

    public Bicicleta cadastrarBicicleta(NovaBicicletaDTO bicicletaDTO) {
        validarBicicleta(bicicletaDTO);
        Bicicleta bicicleta = bicicletaMapper.toEntity(bicicletaDTO);
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
        return bicicletaRepository.save(bicicleta);
    }

    private void validarBicicleta(NovaBicicletaDTO bicicletaDTO) {
        if (bicicletaDTO.getMarca() == null || bicicletaDTO.getModelo() == null
                || bicicletaDTO.getNumero() == null || bicicletaDTO.getAno() == null
                || bicicletaDTO.getMarca().isEmpty() || bicicletaDTO.getModelo().isEmpty()
                || bicicletaDTO.getAno().isEmpty()) {
            throw new InvalidDataException(Constantes.DADOS_INVALIDOS);
        }
    }

    public Bicicleta atualizarBicicleta(Integer idBicicleta, NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = buscarBicicletaPorId(idBicicleta);
        atualizarBicicletaComDTO(bicicleta, bicicletaDTO);
        return bicicletaRepository.save(bicicleta);
    }

    public void excluirBicicleta(Integer idBicicleta) {
        Bicicleta bicicleta = buscarBicicletaPorId(idBicicleta);
        validarStatusParaExclusao(bicicleta);
        bicicletaRepository.deleteById(idBicicleta);
    }

    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    public Bicicleta obterBicicletaPorId(Integer idBicicleta) {
        return buscarBicicletaPorId(idBicicleta);
    }

    public Bicicleta alterarStatusBicicleta(Integer id, String acao) {
        Bicicleta bicicleta = buscarBicicletaPorId(id);
        atualizarStatusBicicleta(bicicleta, acao);
        return bicicletaRepository.save(bicicleta);
    }

    private Bicicleta buscarBicicletaPorId(Integer idBicicleta) {
        return bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));
    }

    private void existsById(Integer idBicicleta) {
        if (!bicicletaRepository.existsById(idBicicleta)) {
            throw new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA);
        }
    }

    private Tranca buscarTrancaPorId(Integer idTranca) {
        return trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    private void atualizarBicicletaComDTO(Bicicleta bicicleta, NovaBicicletaDTO bicicletaDTO) {
        bicicleta.setMarca(bicicletaDTO.getMarca());
        bicicleta.setModelo(bicicletaDTO.getModelo());
        bicicleta.setAno(bicicletaDTO.getAno());
        bicicleta.setNumero(bicicletaDTO.getNumero());
    }

    private void validarStatusParaExclusao(Bicicleta bicicleta) {
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.APOSENTADA || trancaRepository.existsByBicicletaId(bicicleta.getId())) {
            throw new BadRequestException(Constantes.BICICLETA_NAO_APOSENTADA);
        }
    }

    private void validarStatusBicicletaParaIntegracao(Bicicleta bicicleta) {
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.NOVA && bicicleta.getStatusBicicleta() != StatusBicicleta.EM_REPARO) {
            throw new InvalidDataException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }
    }

    private void validarStatusTrancaParaIntegracao(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_DISPONIVEL);
        }
    }

    private void validarFuncionarioParaReparo(Integer idFuncionario) {
        if (!funcionarioService.isFuncionarioValido(idFuncionario)) {
            throw new InvalidDataException(Constantes.FUNCIONARIO_INVALIDO);
        }
    }

    private void associarBicicletaETranca(Bicicleta bicicleta, Tranca tranca, StatusBicicleta statusBicicleta, StatusTranca statusTranca) {
        bicicleta.setStatusBicicleta(statusBicicleta);
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(statusTranca);
        tranca.setBicicleta(bicicleta);
        trancaRepository.save(tranca);
    }

    private void desassociarBicicletaETranca(Bicicleta bicicleta, Tranca tranca, StatusBicicleta statusBicicleta, StatusTranca statusTranca) {
        bicicleta.setStatusBicicleta(statusBicicleta);
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(statusTranca);
        tranca.setBicicleta(null);
        trancaRepository.save(tranca);
    }

    private void validarStatusTrancaParaRemocao(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_OCUPADA);
        }
    }

    private void validarStatusBicicletaParaRemocao(Bicicleta bicicleta, StatusAcaoReparador statusAcaoReparador) {
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new InvalidDataException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }
        if (statusAcaoReparador == null) {
            throw new InvalidDataException(Constantes.ACAO_INVALIDA);
        }
    }

    private StatusBicicleta definirStatusBicicleta(StatusAcaoReparador statusAcaoReparador) {
        if (statusAcaoReparador.equals(StatusAcaoReparador.EM_REPARO)) {
            return StatusBicicleta.EM_REPARO;
        } else if (statusAcaoReparador.equals(StatusAcaoReparador.APOSENTADA)) {
            return StatusBicicleta.APOSENTADA;
        }
        throw new InvalidDataException(Constantes.ACAO_INVALIDA);
    }

    private void atualizarStatusBicicleta(Bicicleta bicicleta, String acao) {
        switch (acao.toLowerCase()) {
            case "disponivel":
                bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
                break;
            case "em uso":
                bicicleta.setStatusBicicleta(StatusBicicleta.EM_USO);
                break;
            case "nova":
                bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
                break;
            case "aposentada":
                bicicleta.setStatusBicicleta(StatusBicicleta.APOSENTADA);
                break;
            case "reparo solicitado":
                bicicleta.setStatusBicicleta(StatusBicicleta.REPARO_SOLICITADO);
                break;
            case "em reparo":
                bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);
                break;
            default:
                throw new InvalidDataException("Ação inválida");
        }
    }

}

