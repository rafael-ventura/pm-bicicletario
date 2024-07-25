package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import org.springframework.stereotype.Service;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;

import java.util.List;

@Service
public class TotemService {

    private final TotemRepository totemRepository;
    private final TotemMapper totemMapper;
    private final TrancaRepository trancaRepository;
    private final BicicletaRepository bicicletaRepository;
    private final TrancaMapper trancaMapper;
    private final BicicletaMapper bicicletaMapper;

    public TotemService(TotemRepository totemRepository, TotemMapper totemMapper, TrancaRepository trancaRepository, BicicletaRepository bicicletaRepository, TrancaMapper trancaMapper, BicicletaMapper bicicletaMapper) {
        this.totemRepository = totemRepository;
        this.totemMapper = totemMapper;
        this.trancaRepository = trancaRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.trancaMapper = trancaMapper;
        this.bicicletaMapper = bicicletaMapper;
    }

    public List<Totem> listarTotens() {
        return totemRepository.findAll();
    }

    public Totem cadastrarTotem(NovoTotemDTO totemDTO) {
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new IllegalArgumentException("Todos os dados do formulário são obrigatórios");
        }
        return totemRepository.save(totemMapper.toEntity(totemDTO));
    }

    public Totem editarTotem(Long idTotem, NovoTotemDTO totemDTO) {
        Totem existente = totemRepository.findById(idTotem).orElseThrow(() -> new IllegalArgumentException("Totem não encontrado"));
        // R2: A informação não pode ser editada.
        if (existente.getId() != null && !existente.getId().equals(idTotem)) {
            throw new IllegalArgumentException("A informação não pode ser editada");
        }
        existente.setLocalizacao(totemDTO.getLocalizacao());
        existente.setDescricao(totemDTO.getDescricao());
        return totemRepository.save(existente);
    }


    public void removerTotem(Long idTotem) {
        totemRepository.findById(idTotem).orElseThrow(() -> new IllegalArgumentException("Totem não encontrado"));
        if (trancaRepository.existsByTotemId(idTotem)) {
            throw new IllegalArgumentException(Constantes.TOTEM_COM_TRANCA);
        }
        totemRepository.deleteById(idTotem);
    }


    public List<Tranca> listarTrancas(Long idTotem) {
        List<Tranca> trancas = trancaRepository.findByTotemId(idTotem);
        return trancaMapper.toDtoList(trancas);
    }

    public List<Bicicleta> listarBicicletas(Long idTotem) {
        List<Bicicleta> bicicletas = bicicletaRepository.findByTotemId(idTotem);
        return bicicletaMapper.toDtoList(bicicletas);
    }

}
