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
        return bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));
    }

    public Bicicleta criarBicicleta(NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = bicicletaMapper.toEntity(bicicletaDTO);
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
        return bicicletaRepository.save(bicicleta);
    }

    public Bicicleta editarBicicleta(Long idBicicleta, NovaBicicletaDTO bicicletaDTO) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));

        bicicleta.setMarca(bicicletaDTO.getMarca());
        bicicleta.setModelo(bicicletaDTO.getModelo());
        bicicleta.setAno(bicicletaDTO.getAno());
        bicicleta.setStatusBicicleta(bicicletaDTO.getStatus());

        return bicicletaRepository.save(bicicleta);
    }

    public void removerBicicleta(Long idBicicleta) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));

        if (bicicleta.getStatusBicicleta() != StatusBicicleta.APOSENTADA) {
            throw new BadRequestException(Constantes.BICICLETA_NAO_APOSENTADA);
        }

        bicicletaRepository.deleteById(idBicicleta);
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));

        if (bicicleta.getStatusBicicleta() != StatusBicicleta.NOVA && bicicleta.getStatusBicicleta() != StatusBicicleta.EM_REPARO) {
            throw new InvalidDataException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_DISPONIVEL);
        }

        if (bicicleta.getStatusBicicleta() == StatusBicicleta.EM_REPARO && !funcionarioService.isFuncionarioValido(dto.getIdFuncionario())) {
            throw new InvalidDataException(Constantes.FUNCIONARIO_INVALIDO);
        }

        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public void retirarDaRede(RetirarBicicletaDaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));

        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_OCUPADA);
        }

        if (bicicleta.getStatusBicicleta() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new InvalidDataException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }

        if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.EM_REPARO)) {
            bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);
        } else if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.APOSENTADA)) {
            bicicleta.setStatusBicicleta(StatusBicicleta.APOSENTADA);
        } else {
            throw new InvalidDataException(Constantes.ACAO_INVALIDA);
        }

        bicicleta.setDataRemocaoTranca(LocalDateTime.now().toString());
        bicicleta.setDataInsercaoTranca(null);
        bicicletaRepository.save(bicicleta);

        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);

        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            throw new InvalidDataException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public Bicicleta alterarStatusBicicleta(Long id, String acao) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));

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
        return bicicletaRepository.save(bicicleta);
    }
}
