package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TotemMapper {
    TotemDTO toDto(Totem totem);
    Totem toEntity(TotemDTO totemDTO);
    List<TotemDTO> toEntityList(List<Totem> totens);
    NovoTotemDTO toDtoNovo(Totem totem);
    Totem toEntity(NovoTotemDTO novoTotemDTO);
}

