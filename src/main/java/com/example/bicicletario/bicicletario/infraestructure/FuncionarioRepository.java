package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FuncionarioRepository {
    private final Map<Long, Funcionario> funcionarios = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(); // Gerador de ID

    public Optional<Funcionario> findById(Long idFuncionario) {
        return Optional.ofNullable(funcionarios.get(idFuncionario));
    }

    public List<Funcionario> findAll() {
        return new ArrayList<>(funcionarios.values());
    }

    public Funcionario save(Funcionario funcionario) {
        if (funcionario.getId() == null) {
            funcionario.setId(idGenerator.incrementAndGet()); // Atribui novo ID se não existir
        }
        funcionarios.put(funcionario.getId(), funcionario);
        return funcionario;
    }

    public void delete(Funcionario funcionario) {
        funcionarios.remove(funcionario.getId());
    }
}
