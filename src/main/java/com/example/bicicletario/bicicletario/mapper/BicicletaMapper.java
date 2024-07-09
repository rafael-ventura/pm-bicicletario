package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.dto.BicicletaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BicicletaMapper {

    @Mapping(target = "id", source = "id")
    Bicicleta toBicicleta(BicicletaDTO dto);

    BicicletaDTO toDto(Bicicleta bicicleta);

    List<BicicletaDTO> toBicicletaDTOs(List<Bicicleta> bicicletas);
}
