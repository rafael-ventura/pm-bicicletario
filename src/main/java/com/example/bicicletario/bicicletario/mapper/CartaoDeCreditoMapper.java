package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartaoDeCreditoMapper {
    CartaoDeCredito toEntity(NovoCartaoDeCreditoDTO dto);
    NovoCartaoDeCreditoDTO toDto(CartaoDeCredito entity);

}
