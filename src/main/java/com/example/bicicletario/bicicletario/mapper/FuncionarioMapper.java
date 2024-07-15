package com.example.bicicletario.bicicletario.mapper;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.FuncionarioDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(target = "matricula", ignore = true)
    Funcionario toEntity(NovoFuncionarioDTO novoFuncionarioDTO);

    FuncionarioDTO toDto(Funcionario funcionario);

    List<FuncionarioDTO> toDtoList(List<Funcionario> funcionarios);
}
