package com.example.bicicletario.bicicletario.application;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));
    }

    public Bicicleta criarBicicleta(NovaBicicletaDTO bicicletaDTO) {
        // [R1] Definir status inicial como "NOVA"
        Bicicleta bicicleta = bicicletaMapper.toEntity(bicicletaDTO);
        bicicleta.setStatusBicicleta(StatusBicicleta.NOVA);
        // [R5] O número da bicicleta é gerado pelo sistema
        return bicicletaRepository.save(bicicleta);
    }

    public Bicicleta editarBicicleta(Long idBicicleta, NovaBicicletaDTO bicicletaDTO) {
        // [A1] Editar bicicleta
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        // [R3] O número da bicicleta não pode ser editado
        bicicleta.setMarca(bicicletaDTO.getMarca());
        bicicleta.setModelo(bicicletaDTO.getModelo());
        bicicleta.setAno(bicicletaDTO.getAno());
        bicicleta.setStatusBicicleta(bicicletaDTO.getStatus());

        return bicicletaRepository.save(bicicleta);
    }

    public void removerBicicleta(Long idBicicleta) {
        // [A2] Remover bicicleta
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        // [R4] Verificar se a bicicleta pode ser excluída
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.APOSENTADA) {
            throw new IllegalArgumentException(Constantes.BICICLETA_NAO_APOSENTADA);
        }

        bicicletaRepository.deleteById(idBicicleta);
    }

    public void integrarNaRede(IntegrarBicicletaNaRedeDTO dto) {
        // [E1] Verificar se a bicicleta existe
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        // [E3] Verificar o status da bicicleta
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.NOVA && bicicleta.getStatusBicicleta() != StatusBicicleta.EM_REPARO) {
            throw new IllegalArgumentException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }

        // [E1] Verificar se a tranca existe
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        // Verificar se a tranca está disponível
        if (tranca.getStatus() != StatusTranca.LIVRE) {
            throw new IllegalArgumentException(Constantes.TRANCA_NAO_DISPONIVEL);
        }

        // [R3] Verificar se o funcionário é válido (em caso de reparo)
        if (bicicleta.getStatusBicicleta() == StatusBicicleta.EM_REPARO && !funcionarioService.isFuncionarioValido(dto.getIdFuncionario())) {
            throw new IllegalArgumentException(Constantes.FUNCIONARIO_INVALIDO);
        }

        // [R1] Registrar data/hora da inserção na tranca, o número da bicicleta e o número da tranca
        bicicleta.setStatusBicicleta(StatusBicicleta.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString());
        bicicletaRepository.save(bicicleta);

        // Alterar status da tranca para ocupada
        tranca.setStatus(StatusTranca.OCUPADA);
        trancaRepository.save(tranca);

        // [R2] Enviar email para o reparador
        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            // [E2] Tratar erro no envio do email
            throw new IllegalArgumentException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public void retirarDaRede(RetirarBicicletaDaRedeDTO dto) {
        // [E1] Validar bicicleta
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.BICICLETA_NAO_ENCONTRADA));

        // [E1] Validar tranca
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new IllegalArgumentException(Constantes.TRANCA_NAO_ENCONTRADA));

        // Verificar se a tranca está ocupada [A2]
        if (tranca.getStatus() != StatusTranca.OCUPADA) {
            throw new IllegalArgumentException(Constantes.TRANCA_NAO_OCUPADA);
        }

        // Verificar se a bicicleta está com status de reparo solicitado
        if (bicicleta.getStatusBicicleta() != StatusBicicleta.REPARO_SOLICITADO) {
            throw new IllegalArgumentException(Constantes.STATUS_DA_BICICLETA_INVALIDO);
        }

        // Atualizar status da bicicleta conforme a escolha do reparador
        if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.EM_REPARO)) {
            bicicleta.setStatusBicicleta(StatusBicicleta.EM_REPARO);
        } else if (dto.getStatusAcaoReparador().equals(StatusAcaoReparador.APOSENTADA)) {
            bicicleta.setStatusBicicleta(StatusBicicleta.APOSENTADA);
        } else {
            throw new IllegalArgumentException(Constantes.ACAO_INVALIDA);
        }

        // [R1] Atualizar a data de remoção da tranca e registrar a operação
        bicicleta.setDataRemocaoTranca(LocalDateTime.now().toString());
        bicicleta.setDataInsercaoTranca(null);
        bicicletaRepository.save(bicicleta);

        // Atualizar o status da tranca
        tranca.setStatus(StatusTranca.LIVRE);
        trancaRepository.save(tranca);

        // [R2] Enviar email para o reparador
        try {
            emailService.enviarEmailParaReparador(dto.getIdFuncionario());
        } catch (Exception e) {
            // [E2] Tratar erro no envio do email
            throw new IllegalArgumentException(Constantes.ERROR_ENVIAR_EMAIL);
        }
    }

    public Bicicleta alterarStatusBicicleta(Long id, String acao) {
        Optional<Bicicleta> optionalBicicleta = bicicletaRepository.findById(id);
        if (optionalBicicleta.isPresent()) {
            Bicicleta bicicleta = optionalBicicleta.get();
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
                    throw new IllegalArgumentException("Ação inválida");
            }
            return bicicletaRepository.save(bicicleta);
        } else {
            throw new IllegalArgumentException("Bicicleta não encontrada");
        }
    }
}
