package com.example.bicicletario.bicicletario.application.services;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.mapper.TotemMapper;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotemService {

    private final TrancaRepository trancaRepository;
    private final BicicletaRepository bicicletaRepository;
    private final TotemRepository totemRepository;
    private final TotemMapper totemMapper;

    public TotemService(TotemMapper totemMapper, TotemRepository totemRepository, BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository) {
        this.totemMapper = totemMapper;
        this.totemRepository = totemRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.trancaRepository = trancaRepository;
    }

    // Métodos públicos

    public List<Totem> listarTodosTotens() {
        return totemRepository.findAll();
    }

    public Totem cadastrarNovoTotem(NovoTotemDTO totemDTO) {
        validarDadosTotem(totemDTO);
        Totem novoTotem = totemMapper.toEntity(totemDTO);
        return totemRepository.save(novoTotem);
    }

    public Totem atualizarTotem(Long idTotem, NovoTotemDTO totemDTO) {
        Totem totemExistente = buscarTotemPorId(idTotem);
        validarDadosTotem(totemDTO);
        atualizarDadosTotem(totemExistente, totemDTO);
        return totemRepository.save(totemExistente);
    }

    public void excluirTotem(Long idTotem) {
        Totem totem = buscarTotemPorId(idTotem);
        validarExclusaoDeTotem(totem);
        totemRepository.deleteById(idTotem);
    }

    public List<Tranca> listarTrancasPorTotem(Long idTotem) {
        verificarExistenciaTotem(idTotem);
        return trancaRepository.findByTotemId(idTotem);
    }

    public List<Bicicleta> listarBicicletasPorTotem(Long idTotem) {
        verificarExistenciaTotem(idTotem);
        return bicicletaRepository.findByTotemId(idTotem);
    }

    // Métodos auxiliares privados

    private Totem buscarTotemPorId(Long idTotem) {
        return totemRepository.findById(idTotem)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TOTEM_NAO_ENCONTRADO));
    }

    private void verificarExistenciaTotem(Long idTotem) {
        if (!totemRepository.existsById(idTotem)) {
            throw new ResourceNotFoundException(Constantes.TOTEM_NAO_ENCONTRADO);
        }
    }

    private void validarDadosTotem(NovoTotemDTO totemDTO) {
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new InvalidDataException(Constantes.DADOS_INVALIDOS);
        }
        // Aqui poderíamos adicionar validações para garantir que os dados não sejam editados após o cadastro (Regra R2)
    }

    private void atualizarDadosTotem(Totem totemExistente, NovoTotemDTO totemDTO) {
        // Assumindo que somente localização e descrição são editáveis
        totemExistente.setLocalizacao(totemDTO.getLocalizacao());
        totemExistente.setDescricao(totemDTO.getDescricao());
    }

    private void validarExclusaoDeTotem(Totem totem) {
        // Regra R3 - Apenas totens que não possuem nenhuma tranca podem ser excluídos
        List<Tranca> trancasAssociadas = listarTrancasPorTotem(totem.getId());
        if (!trancasAssociadas.isEmpty()) {
            throw new InvalidDataException(Constantes.TOTEM_NAO_ENCONTRADO);
        }
    }
}
