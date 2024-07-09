package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import com.example.bicicletario.bicicletario.domain.dto.TotemDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TotemMapper {
    TotemDTO toTotemDTO(Totem totem);
    Totem toTotem(TotemDTO totemDTO);
    List<TotemDTO> toTotemDTOs(List<Totem> totens);
    NovoTotemDTO toNovoTotemDTO(Totem totem);
    Totem toTotem(NovoTotemDTO novoTotemDTO);
}

