package com.example.bicicletario.bicicletario.domain.mapper;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import com.example.bicicletario.bicicletario.domain.dto.NovoFuncionarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(target = "matricula", ignore = true)
    Funcionario toEntity(NovoFuncionarioDTO novoFuncionarioDTO);

    List<Funcionario> toEntityList(List<NovoFuncionarioDTO> novoFuncionarioDTO);

    NovoFuncionarioDTO toDto(Funcionario funcionario);

    List<NovoFuncionarioDTO> toDtoList(List<Funcionario> funcionarios);
}
