package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrancaMapper {

    Tranca toEntity(NovaTrancaDTO dto);
    TrancaDTO toDto(Tranca tranca);
    IntegrarBicicletaNaRedeDTO toDtoIntegrarBicicleta(Tranca tranca);
    RetirarBicicletaDaRedeDTO toDtoRetirarBicicleta(Tranca tranca);

    List<TrancaDTO> toDtoList(List<Tranca> trancas);
}

