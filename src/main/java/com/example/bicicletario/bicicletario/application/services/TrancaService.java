package com.example.bicicletario.bicicletario.application.services;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarTrancaDaRedeDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusAcaoReparador;
import com.example.bicicletario.bicicletario.domain.enums.StatusTranca;
import com.example.bicicletario.bicicletario.domain.mapper.TrancaMapper;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrancaService {

    private final TrancaRepository trancaRepository;
    private final TrancaMapper trancaMapper;
    private final EmailService emailService;
    private final FuncionarioService funcionarioService;
    private final BicicletaRepository bicicletaRepository;

    public TrancaService(TrancaRepository trancaRepository, TrancaMapper trancaMapper,
                         EmailService emailService, FuncionarioService funcionarioService, BicicletaRepository bicicletaRepository) {
        this.trancaRepository = trancaRepository;
        this.trancaMapper = trancaMapper;
        this.emailService = emailService;
        this.funcionarioService = funcionarioService;
        this.bicicletaRepository = bicicletaRepository;
    }

    public void incluirTrancaNaRede(IntegrarBicicletaNaRedeDTO dto) {
        Tranca tranca = obterTrancaPorId(dto.getIdTranca());

        // Validações de status e funcionário
        validarCondicoesParaInclusao(tranca, dto.getIdFuncionario());

        // Verificar se o funcionário que está devolvendo a tranca é o mesmo que retirou para reparo
        if (tranca.getStatus() == StatusTranca.EM_REPARO) {
            validarFuncionarioParaReparo(tranca, dto.getIdFuncionario());
        }

        // Atualizar status e informações da tranca
        atualizarTrancaParaInclusao(tranca, dto.getIdFuncionario());

        // Enviar e-mail
        emailService.enviarEmailParaTranca(dto.getIdFuncionario(), tranca, "Inclusão");
    }

    public void retirarTrancaDaRede(RetirarTrancaDaRedeDTO dto) {
        Tranca tranca = obterTrancaPorId(dto.getIdTranca());

        // Usando métodos auxiliares para validação e atualização
        validarCondicoesParaRetirada(tranca, dto.getStatusAcaoReparador());
        atualizarTrancaParaRetirada(tranca, dto.getIdFuncionario(), dto.getStatusAcaoReparador());

        // Enviar e-mail
        emailService.enviarEmailParaTranca(dto.getIdFuncionario(), tranca, "Retirada");
    }


    public Tranca cadastrarNovaTranca(NovaTrancaDTO trancaDTO) {
        validarDadosTranca(trancaDTO);
        Tranca novaTranca = trancaMapper.toEntity(trancaDTO);
        novaTranca.setStatus(StatusTranca.NOVA);
        return trancaRepository.save(novaTranca);
    }

    public Tranca atualizarTranca(Integer idTranca, NovaTrancaDTO trancaDTO) {
        Tranca trancaExistente = obterTrancaPorId(idTranca);
        validarDadosTranca(trancaDTO);
        atualizarDadosTranca(trancaExistente, trancaDTO);
        return trancaRepository.save(trancaExistente);
    }

    public void excluirTranca(Integer idTranca) {
        Tranca tranca = obterTrancaPorId(idTranca);
        validarExclusaoDeTranca(tranca);
        tranca.setStatus(StatusTranca.EXCLUIDA);
        trancaRepository.save(tranca);
    }

    public List<Tranca> listarTodasTrancas() {
        return trancaRepository.findAll();
    }

    public Tranca obterTrancaPorId(Integer idTranca) {
        return trancaRepository.findById(idTranca)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TRANCA_NAO_ENCONTRADA));
    }

    public Tranca obterBicicletaNaTranca(Integer idTranca) {
        Tranca tranca = obterTrancaPorId(idTranca);
        verificarBicicletaNaTranca(tranca);
        return tranca;
    }

    public Tranca trancarTranca(Integer idTranca, Integer bicicletaId) {
        Tranca tranca = obterTrancaPorId(idTranca);
        trancarBicicletaETranca(tranca, bicicletaId);
        return tranca;
    }

    public Tranca destrancarTranca(Integer idTranca, Integer bicicletaId) {
        Tranca tranca = obterTrancaPorId(idTranca);
        removerBicicletaDaTranca(tranca, bicicletaId);
        return tranca;
    }

    public Tranca alterarStatusTranca(Integer idTranca, String acao) {
        Tranca tranca = obterTrancaPorId(idTranca);
        atualizarStatusTranca(tranca, acao);
        return tranca;
    }

    // Métodos auxiliares privados encapsulados

    private void trancarBicicletaETranca(Tranca tranca, Integer bicicletaId) {
        if (bicicletaId != null) {
            Bicicleta bicicleta = buscarBicicletaPorId(bicicletaId);
            tranca.setBicicleta(bicicleta);
            tranca.setStatus(StatusTranca.OCUPADA);
            trancaRepository.save(tranca);
        } else {
            throw new InvalidDataException(Constantes.BICICLETA_NAO_ENCONTRADA);
        }
    }

    private void removerBicicletaDaTranca(Tranca tranca, Integer bicicletaId) {
        if (tranca.getBicicleta() != null && tranca.getBicicleta().getId().equals(bicicletaId)) {
            tranca.setBicicleta(null);
            tranca.setStatus(StatusTranca.LIVRE);
            trancaRepository.save(tranca);
        } else {
            throw new InvalidDataException(Constantes.BICICLETA_NAO_ENCONTRADA);
        }
    }

    private void atualizarStatusTranca(Tranca tranca, String acao) {
        switch (acao.toLowerCase()) {
            case "trancar":
                tranca.setStatus(StatusTranca.OCUPADA);
                break;
            case "destrancar":
                tranca.setStatus(StatusTranca.LIVRE);
                break;
            default:
                throw new InvalidDataException(Constantes.ERRO_ALTERAR_STATUS_TRANCA);
        }
        trancaRepository.save(tranca);
    }

    private Bicicleta buscarBicicletaPorId(Integer bicicletaId) {
        return bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.BICICLETA_NAO_ENCONTRADA));
    }

    private void verificarBicicletaNaTranca(Tranca tranca) {
        if (tranca.getBicicleta() == null) {
            throw new InvalidDataException(Constantes.BICICLETA_NAO_ENCONTRADA);
        }
    }

    private void validarDadosTranca(NovaTrancaDTO trancaDTO) {
        if (trancaDTO.getLocalizacao() == null || trancaDTO.getModelo() == null || trancaDTO.getAnoDeFabricacao() == null) {
            throw new InvalidDataException(Constantes.DADOS_INVALIDOS);
        }
    }

    private void atualizarDadosTranca(Tranca trancaExistente, NovaTrancaDTO trancaDTO) {
        trancaExistente.setNumero(trancaDTO.getNumero());
        trancaExistente.setModelo(trancaDTO.getModelo());
        trancaExistente.setAnoDeFabricacao(trancaDTO.getAnoDeFabricacao());
        trancaExistente.setStatus(trancaDTO.getStatus());
    }

    private void validarExclusaoDeTranca(Tranca tranca) {
        if (trancaTemBicicleta(tranca)) {
            throw new InvalidDataException(Constantes.TRANCA_PRENCHIDA);
        }
    }

    private boolean trancaTemBicicleta(Tranca tranca) {
        return tranca.getStatus() == StatusTranca.OCUPADA;
    }

    void atualizarTrancaParaInclusao(Tranca tranca, Integer idFuncionario) {
        tranca.setStatus(StatusTranca.LIVRE);
        tranca.setDataInsercaoTotem(LocalDateTime.now().toString());
        tranca.setIdFuncionarioUltimaOperacao(idFuncionario);
        trancaRepository.save(tranca);
    }

    private void atualizarTrancaParaRetirada(Tranca tranca, Integer idFuncionario, StatusAcaoReparador statusAcaoReparador) {
        tranca.setIdFuncionarioUltimaOperacao(idFuncionario);
        if (statusAcaoReparador.equals(StatusAcaoReparador.EM_REPARO)) {
            tranca.setStatus(StatusTranca.EM_REPARO);
        } else if (statusAcaoReparador.equals(StatusAcaoReparador.APOSENTADA)) {
            tranca.setStatus(StatusTranca.APOSENTADA);
        }
        trancaRepository.save(tranca);
    }

    private void validarCondicoesParaInclusao(Tranca tranca, Integer idFuncionario) {
        if (tranca.getStatus() != StatusTranca.NOVA && tranca.getStatus() != StatusTranca.EM_REPARO) {
            throw new InvalidDataException(Constantes.TRANCA_NAO_DISPONIVEL);
        }
        if (tranca.getStatus() == StatusTranca.EM_REPARO && !funcionarioService.isFuncionarioValido(idFuncionario)) {
            throw new InvalidDataException(Constantes.FUNCIONARIO_INVALIDO);
        }
    }

    private void validarCondicoesParaRetirada(Tranca tranca, StatusAcaoReparador statusAcaoReparador) {
        if (trancaTemBicicleta(tranca)) {
            throw new InvalidDataException(Constantes.TRANCA_PRENCHIDA);
        }
        if (statusAcaoReparador == null) {
            throw new InvalidDataException(Constantes.STATUS_DE_ACAO_REPARADOR_INVALIDO);
        }
    }

    private void validarFuncionarioParaReparo(Tranca tranca, Integer idFuncionario) {
        if (!tranca.getIdFuncionarioUltimaOperacao().equals(idFuncionario)) {
            throw new InvalidDataException(Constantes.FUNCIONARIO_INVALIDO);
        }
    }
}
