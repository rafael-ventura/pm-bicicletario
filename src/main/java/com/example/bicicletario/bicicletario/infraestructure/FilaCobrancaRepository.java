package com.example.bicicletario.bicicletario.infraestructure;

import com.example.bicicletario.bicicletario.domain.Cobranca;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Queue;

@Repository
public class FilaCobrancaRepository {
    private Queue<Cobranca> filaCobranca = new LinkedList<>();

    public void adicionarNaFila(Cobranca cobranca) {
        filaCobranca.add(cobranca);
    }

    public Cobranca removerDaFila() {
        return filaCobranca.poll();
    }

    public Queue<Cobranca> obterFila() {
        return new LinkedList<>(filaCobranca);
    }

    public boolean isEmpty() {
        return filaCobranca.isEmpty();
    }
}
