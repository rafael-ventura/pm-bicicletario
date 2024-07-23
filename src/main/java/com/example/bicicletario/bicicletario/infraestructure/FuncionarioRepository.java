package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FuncionarioRepository {
    private final Map<Long, Funcionario> funcionarios = new HashMap<>();

    public Optional<Funcionario> findById(Long idFuncionario) {
        return Optional.ofNullable(funcionarios.get(idFuncionario));
    }

    public List<Funcionario> findAll() {
        return new ArrayList<>(funcionarios.values());
    }

    public Funcionario save(Funcionario funcionario) {
        funcionarios.put(funcionario.getId(), funcionario);
        return funcionario;
    }

    public void delete(Funcionario funcionario) {
        funcionarios.remove(funcionario.getId());
    }
}