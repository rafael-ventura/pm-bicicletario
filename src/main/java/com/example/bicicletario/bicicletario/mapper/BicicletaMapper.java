package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.dto.IntegrarNaRedeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BicicletaMapper {

    @Mapping(target = "idTranca", source = "dto.idTranca")
    Bicicleta toBicicleta(IntegrarNaRedeDTO dto);

    IntegrarNaRedeDTO toDto(Bicicleta bicicleta);
}
