package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Passaporte;
import com.example.bicicletario.bicicletario.domain.dto.PassaporteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassaporteMapper {
    PassaporteDTO toDTO(Passaporte passaporte);
    Passaporte toEntity(PassaporteDTO passaporteDTO);
}
