package com.example.bicicletario.bicicletario.application;

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
    private final EmailService emailService;
    private final FuncionarioService funcionarioService;

    public BicicletaService(BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository, BicicletaMapper bicicletaMapper, EmailService emailService, FuncionarioService funcionarioService) {
        this.bicicletaRepository = bicicletaRepository;
        this.trancaRepository = trancaRepository;
        this.bicicletaMapper = bicicletaMapper;
        this.emailService = emailService;
        this.funcionarioService = funcionarioService;
    }

    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    public Bicicleta obterBicicleta(Long idBicicleta) {
        return findBicicletaByIdOrThrow(idBicicleta);
    }

    public Bicicleta criarBicicleta(NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = bicicletaMapper.toEntity(bicicletaDTO);
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
        return bicicletaRepository.save(bicicleta);
    }

    public Bicicleta editarBicicleta(Long idBicicleta, NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = findBicicletaByIdOrThrow(idBicicleta);
        updateBicicletaComDTO(bicicleta, bicicletaDTO);
        return bicicletaRepository.save(bicicleta);
    }

    public void removerBicicleta(Long idBicicleta) {
        Bicicleta bicicleta = findBicicletaByIdOrThrow(idBicicleta);
        validaBicicletaStatusParaRemocao(bicicleta);
        bicicletaRepository.deleteById(idBicicleta);
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Bicicleta bicicleta = findBicicletaByIdOrThrow(dto.getIdBicicleta());
        Tranca tranca = findTrancaByIdOrThrow(dto.getIdTranca());

        validaBicicletaStatusParaIntegracao(bicicleta);
        validaTrancaStatusParaIntegracao(tranca);

        if (bicicleta.getStatusBicicleta() == StatusBicicleta.EM_REPARO) {
            validaFuncionarioParaReparo(dto.getIdFuncionario());
        }

        updateBicicletaETrancaParaIntegracao(bicicleta, tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public void retirarDaRede(RetirarBicicletaDaRedeDTO dto) {
        Bicicleta bicicleta = findBicicletaByIdOrThrow(dto.getIdBicicleta());
        Tranca tranca = findTrancaByIdOrThrow(dto.getIdTranca());

        validaTrancaStatusParaRemover(tranca);
        validaBicicletaStatusParaRemover(bicicleta, dto.getStatusAcaoReparador());

        updateBicicletaETrancaParaRemover(bicicleta, tranca, dto.getStatusAcaoReparador());

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public Bicicleta alterarStatusBicicleta(Long id, String acao) {
        Bicicleta bicicleta = findBicicletaByIdOrThrow(id);
        updateBicicletaStatus(bicicleta, acao);
        return bicicletaRepository.save(bicicleta);
    }

    private Bicicleta findBicicletaByIdOrThrow(Long idBicicleta) {
        return bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));
    }

    private Tranca findTrancaByIdOrThrow(Long idTranca) {
        return trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    private void updateBicicletaComDTO(Bicicleta bicicleta, NovaBicicletaDTO bicicletaDTO) {
        bicicleta.setMarca(bicicletaDTO.getMarca());
        bicicleta.setModelo(bicicletaDTO.getModelo());
        bicicleta.setAno(bicicletaDTO.getAno());
        bicicleta.setStatusBicicleta(bicicletaDTO.getStatus());
    }

    private void validaBicicletaStatusParaRemocao(Bicicleta bicicleta) {
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.APOSENTADA) {
            throw new BadRequestException(Constantes.BICICLETA_NAO_APOSENTADA);
        }
    }

    private void validaBicicletaStatusParaIntegracao(Bicicleta bicicleta) {
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.NOVA && bicicleta.getStatusBicicleta() != StatusBicicleta.EM_REPARO) {
            throw new InvalidDataException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }
    }

    private void validaTrancaStatusParaIntegracao(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_DISPONIVEL);
        }
    }

    private void validaFuncionarioParaReparo(Long idFuncionario) {
        if (!funcionarioService.isFuncionarioValido(idFuncionario)) {
            throw new InvalidDataException(Constantes.FUNCIONARIO_INVALIDO);
        }
    }

    private void updateBicicletaETrancaParaIntegracao(Bicicleta bicicleta, Tranca tranca) {
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);
    }

    private void validaTrancaStatusParaRemover(Tranca tranca) {
        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_OCUPADA);
        }
    }

    private void validaBicicletaStatusParaRemover(Bicicleta bicicleta, StatusAcaoReparador statusAcaoReparador) {
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new InvalidDataException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }
        if (statusAcaoReparador == null) {
            throw new InvalidDataException(Constantes.ACAO_INVALIDA);
        }
    }

    private void updateBicicletaETrancaParaRemover(Bicicleta bicicleta, Tranca tranca, StatusAcaoReparador statusAcaoReparador) {
        if (statusAcaoReparador.equals(StatusAcaoReparador.EM_REPARO)) {
            bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);
        } else if (statusAcaoReparador.equals(StatusAcaoReparador.APOSENTADA)) {
            bicicleta.setStatusBicicleta(StatusBicicleta.APOSENTADA);
        }
        bicicleta.setDataRemocaoTranca(LocalDateTime.now().toString());
        bicicleta.setDataInsercaoTranca(null);
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);
    }

    private void updateBicicletaStatus(Bicicleta bicicleta, String acao) {
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
