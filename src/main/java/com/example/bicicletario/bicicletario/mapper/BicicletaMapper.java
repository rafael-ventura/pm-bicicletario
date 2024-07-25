package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.models.Bicicleta;
import com.example.bicicletario.bicicletario.domain.dto.NovaBicicletaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BicicletaMapper {

    Bicicleta toEntity(NovaBicicletaDTO dto);
    List<Bicicleta> toDtoList(List<Bicicleta> bicicletas);
}
