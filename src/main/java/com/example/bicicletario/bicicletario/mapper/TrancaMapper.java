package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarBicicletaDaRedeDTO;
import com.example.bicicletario.bicicletario.dto.TrancaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrancaMapper {

    Tranca toTranca(TrancaDTO dto);
    TrancaDTO toDto(Tranca tranca);

    IntegrarBicicletaNaRedeDTO toDtoFromTranca(Tranca tranca);
    RetirarBicicletaDaRedeDTO toDtoFromRetirarTranca(Tranca tranca);
}
