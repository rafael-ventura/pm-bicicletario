package com.example.bicicletario.bicicletario.mapper;


import com.example.bicicletario.bicicletario.domain.CartaoDeCredito;
import com.example.bicicletario.bicicletario.domain.dto.CartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartaoDeCreditoMapper {


    CartaoDeCreditoDTO toDto(CartaoDeCredito entity);

    CartaoDeCredito toEntity(CartaoDeCreditoDTO dto);

    CartaoDeCredito toEntity(NovoCartaoDeCreditoDTO dto);
}
