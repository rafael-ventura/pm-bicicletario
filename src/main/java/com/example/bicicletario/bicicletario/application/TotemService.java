package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.exceptions.InvalidDataException;
import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.constants.Constantes;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TotemRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import com.example.bicicletario.bicicletario.mapper.TotemMapper;
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

    public List<Totem> listarTotens() {
        return totemRepository.findAll();
    }

    public Totem cadastrarTotem(NovoTotemDTO totemDTO) {
        validarTotemDTO(totemDTO);
        return totemRepository.save(totemMapper.toEntity(totemDTO));
    }

    public Totem editarTotem(Long idTotem, NovoTotemDTO totemDTO) {
        Totem existente = totemRepository.findById(idTotem)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.TOTEM_NAO_ENCONTRADO));
        validarTotemDTO(totemDTO);
        existente.setLocalizacao(totemDTO.getLocalizacao());
        existente.setDescricao(totemDTO.getDescricao());
        return totemRepository.save(existente);
    }

    public void removerTotem(Long idTotem) {
        if (!totemRepository.existsById(idTotem)) {
            throw new ResourceNotFoundException(Constantes.TOTEM_NAO_ENCONTRADO);
        }
        totemRepository.deleteById(idTotem);
    }

    public List<Tranca> listarTrancas(Long idTotem) {
        if (!totemRepository.existsById(idTotem)) {
            throw new ResourceNotFoundException(Constantes.TOTEM_NAO_ENCONTRADO);
        }
        return trancaRepository.findByTotemId(idTotem);
    }

    public List<Bicicleta> listarBicicletas(Long idTotem) {
        if (!totemRepository.existsById(idTotem)) {
            throw new ResourceNotFoundException(Constantes.TOTEM_NAO_ENCONTRADO);
        }
        return bicicletaRepository.findByTotemId(idTotem);
    }

    private void validarTotemDTO(NovoTotemDTO totemDTO) {
        if (totemDTO.getLocalizacao() == null || totemDTO.getDescricao() == null) {
            throw new InvalidDataException(Constantes.DADOS_INVALIDOS);
        }
    }
}
