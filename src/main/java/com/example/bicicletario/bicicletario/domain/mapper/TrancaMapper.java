package com.example.bicicletario.bicicletario.domain.mapper;

import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrancaMapper {

    Tranca toEntity(NovaTrancaDTO dto);
    List<Tranca> toDtoList(List<Tranca> trancas);
}

