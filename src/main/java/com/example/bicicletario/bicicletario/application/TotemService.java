package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.BicicletaMapper;
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
import com.example.bicicletario.bicicletario.mapper.TrancaMapper;
import org.springframework.stereotype.Service;

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

    public List<TotemDTO> listarTotens() {
        return totemMapper.toEntityList(totemRepository.findAll());
    }

    public TotemDTO cadastrarTotem(NovoTotemDTO totemDTO) {
        // R1: Todos os dados do formulário são obrigatórios.
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new IllegalArgumentException("Todos os dados do formulário são obrigatórios");
        }
        Totem totem = totemMapper.toEntity(totemDTO);
        totem = totemRepository.save(totem);
        return totemMapper.toDto(totem);
    }

    public TotemDTO editarTotem(Long idTotem, TotemDTO totemDTO) {
        Totem existente = totemRepository.findById(idTotem).orElseThrow(() -> new IllegalArgumentException("Totem não encontrado"));
        // R2: A informação não pode ser editada.
        if (totemDTO.getId() != null && !totemDTO.getId().equals(idTotem)) {
            throw new IllegalArgumentException("A informação não pode ser editada");
        }
        existente.setLocalizacao(totemDTO.getLocalizacao());
        existente.setDescricao(totemDTO.getDescricao());
        return totemMapper.toDto(totemRepository.save(existente));
    }

    public void removerTotem(Long idTotem) {
        Totem totem = totemRepository.findById(idTotem).orElseThrow(() -> new IllegalArgumentException("Totem não encontrado"));
        // R3: Apenas os totens que não possuem nenhuma tranca podem ser excluídos.
        if (trancaRepository.existsByTotemId(idTotem)) {
            throw new IllegalArgumentException("Totem possui trancas associadas e não pode ser removido");
        }
        totemRepository.delete(totem);
    }

    public List<TrancaDTO> listarTrancas(Long idTotem) {
        List<Tranca> trancas = trancaRepository.findByTotemId(idTotem);
        return trancaMapper.toDtoList(trancas);
    }

    public List<BicicletaDTO> listarBicicletas(Long idTotem) {
        List<Bicicleta> bicicletas = bicicletaRepository.findByTotemId(idTotem);
        return bicicletaMapper.toDtoList(bicicletas);
    }
}
