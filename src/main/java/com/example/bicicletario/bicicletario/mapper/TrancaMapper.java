package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.dto.IntegrarNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarDaRedeDTO;
import com.example.bicicletario.bicicletario.dto.TrancaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrancaMapper {

    Tranca toTranca(TrancaDTO dto);
    TrancaDTO toDto(Tranca tranca);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status")
    Tranca toTranca(IntegrarNaRedeDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "statusAcaoReparador")
    Tranca toTranca(RetirarDaRedeDTO dto);

    IntegrarNaRedeDTO toDtoFromTranca(Tranca tranca);
    RetirarDaRedeDTO toDtoFromRetirarTranca(Tranca tranca);
}
