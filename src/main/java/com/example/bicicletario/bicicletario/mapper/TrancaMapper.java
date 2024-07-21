package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.models.Tranca;
import com.example.bicicletario.bicicletario.domain.dto.IntegrarBicicletaNaRedeDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovaTrancaDTO;
import com.example.bicicletario.bicicletario.domain.dto.RetirarBicicletaDaRedeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrancaMapper {

    Tranca toEntity(NovaTrancaDTO dto);
    IntegrarBicicletaNaRedeDTO toDtoIntegrarBicicleta(Tranca tranca);
    RetirarBicicletaDaRedeDTO toDtoRetirarBicicleta(Tranca tranca);
    List<Tranca> toDtoList(List<Tranca> trancas);
}

