package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Funcionario;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class FuncionarioRepository {
    private final Map<Integer, Funcionario> funcionarios = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // Gerador de ID

    public Optional<Funcionario> findById(Integer idFuncionario) {
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

    public boolean existsById(int l) {
        return funcionarios.containsKey(l);
    }

    public void deleteById(int i) {
        funcionarios.remove(i);
    }
}
