package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BicicletaMapper {

    @Mapping(target = "id", source = "id")
    Bicicleta toEntity(NovaBicicletaDTO dto);

    NovaBicicletaDTO toDto(Bicicleta bicicleta);

    List<NovaBicicletaDTO> toDtoList(List<Bicicleta> bicicletas);
}
