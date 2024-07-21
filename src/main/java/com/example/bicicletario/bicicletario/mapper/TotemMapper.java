package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Totem;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotemMapper {
    NovoTotemDTO toDto(Totem totem);
    NovoTotemDTO toDtoNovo(Totem totem);
    Totem toEntity(NovoTotemDTO novoTotemDTO);
}

