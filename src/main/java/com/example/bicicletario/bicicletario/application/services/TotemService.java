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

import java.util.ArrayList;
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

    public Totem atualizarTotem(Integer idTotem, NovoTotemDTO totemDTO) {
        Totem totemExistente = totemRepository.findById(idTotem)
                .orElseThrow(() -> new ResourceNotFoundException("Totem não encontrado"));
        validarDadosTotem(totemDTO);
        atualizarDadosTotem(totemExistente, totemDTO);
        return totemRepository.save(totemExistente);
    }

    public void excluirTotem(Integer idTotem) {
        Totem totem = buscarTotemPorId(idTotem);
        validarExclusaoDeTotem(totem);
        totemRepository.deleteById(idTotem);
    }

    public List<Tranca> listarTrancasPorTotem(Integer idTotem) {
        Totem totem = recuperarTotem(idTotem);
        List<Tranca> trancas = new ArrayList<>();
        try {
            trancas = trancaRepository.findTrancaByLocalizacao(totem.getLocalizacao());
        } catch (InvalidDataException e) {
            throw new ResourceNotFoundException(Constantes.DADOS_INVALIDOS);
        }
        if (trancas.isEmpty()) {
            throw new ResourceNotFoundException(Constantes.NAO_ENCONTRADO);
        }
        return trancas;
    }

    public List<Bicicleta> listarBicicletasPorTotem(Integer idTotem) {
        List<Bicicleta> bicicletas = new ArrayList<>();
        List<Tranca> trancas = listarTrancasPorTotem(idTotem);
        try {
            trancas.forEach(tranca -> bicicletas.add(tranca.getBicicleta()));
        } catch (InvalidDataException e) {
            throw new ResourceNotFoundException(Constantes.DADOS_INVALIDOS);
        }
        if (bicicletas.isEmpty()) {
            throw new ResourceNotFoundException(Constantes.NAO_ENCONTRADO);
        }
        return bicicletas;
    }

    private Totem buscarTotemPorId(Integer idTotem) {
        return totemRepository.findById(idTotem)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.NAO_ENCONTRADO));
    }

    private Totem recuperarTotem(Integer idTotem) {
        if (!totemRepository.existsById(idTotem)) {
            throw new ResourceNotFoundException(Constantes.NAO_ENCONTRADO);
        }
        return totemRepository.get(idTotem);
    }

    private void validarDadosTotem(NovoTotemDTO totemDTO) {
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new InvalidDataException(Constantes.DADOS_INVALIDOS);
        }
    }

    private void atualizarDadosTotem(Totem totemExistente, NovoTotemDTO totemDTO) {
        totemExistente.setLocalizacao(totemDTO.getLocalizacao());
        totemExistente.setDescricao(totemDTO.getDescricao());
    }

    private void validarExclusaoDeTotem(Totem totem) {

        List<Tranca> trancasAssociadas = listarTrancasPorTotem(totem.getId());
        if (!trancasAssociadas.isEmpty()) {
            throw new InvalidDataException(Constantes.TOTEM_NAO_ENCONTRADO);
        }
    }
}
