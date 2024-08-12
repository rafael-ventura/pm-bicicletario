package com.example.bicicletario.bicicletario.domain.mapper;

import com.example.bicicletario.bicicletario.domain.models.Totem;
import com.example.bicicletario.bicicletario.domain.dto.NovoTotemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotemMapper {
    Totem toEntity(NovoTotemDTO novoTotemDTO);
}

