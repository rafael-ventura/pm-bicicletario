package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.dto.NovoCiclistaDTO;
import com.example.bicicletario.bicicletario.domain.dto.CiclistaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CiclistaMapper {

    Ciclista toDto(NovoCiclistaDTO dto);

    CiclistaDTO toDto (Ciclista dto);

    Ciclista toEntity(NovoCiclistaDTO novoDto);

    Ciclista toEntity(CiclistaDTO dto);
}


