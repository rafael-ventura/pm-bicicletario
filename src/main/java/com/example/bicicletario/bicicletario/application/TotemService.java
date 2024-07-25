package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotemService {

    @Autowired
    private TrancaRepository trancaRepository;
    @Autowired
    private BicicletaRepository bicicletaRepository;
    @Autowired
    private TotemRepository totemRepository;
    @Autowired
    private TotemMapper totemMapper;

    public List<Totem> listarTotens() {
        return totemRepository.findAll();
    }

    public Totem cadastrarTotem(NovoTotemDTO totemDTO) {
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new InvalidDataException("Todos os dados do formulário são obrigatórios");
        }
        return totemRepository.save(totemMapper.toEntity(totemDTO));
    }

    public Totem editarTotem(Long idTotem, NovoTotemDTO totemDTO) {
        Totem existente = totemRepository.findById(idTotem)
                .orElseThrow(() -> new ResourceNotFoundException("Totem não encontrado"));
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new InvalidDataException("Todos os dados do formulário são obrigatórios");
        }
        existente.setLocalizacao(totemDTO.getLocalizacao());
        existente.setDescricao(totemDTO.getDescricao());
        return totemRepository.save(existente);
    }

    public void removerTotem(Long idTotem) {
        Totem totem = totemRepository.findById(idTotem)
                .orElseThrow(() -> new ResourceNotFoundException("Totem não encontrado"));
        totemRepository.deleteById(idTotem);
    }

    public List<Tranca> listarTrancas(Long idTotem) {
        return totemRepository.findById(idTotem)
                .map(totem -> trancaRepository.findByTotemId(idTotem))
                .orElseThrow(() -> new ResourceNotFoundException("Totem não encontrado"));
    }

    public List<Bicicleta> listarBicicletas(Long idTotem) {
        return totemRepository.findById(idTotem)
                .map(totem -> bicicletaRepository.findByTotemId(idTotem))
                .orElseThrow(() -> new ResourceNotFoundException("Totem não encontrado"));
    }
}
