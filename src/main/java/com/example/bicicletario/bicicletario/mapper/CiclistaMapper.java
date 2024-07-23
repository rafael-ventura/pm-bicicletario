package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CiclistaMapper {
    @Mapping(target = "id", ignore = true)
    Ciclista toEntity(NovoCiclistaDTO dto);
}


