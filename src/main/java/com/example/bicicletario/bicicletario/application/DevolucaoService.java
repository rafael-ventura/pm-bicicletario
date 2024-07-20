package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Devolucao;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class DevolucaoService {

    private final AluguelRepository aluguelRepository;
    private final CiclistaService ciclistaService;
    private final CiclistaRepository ciclistaRepository;

    @Autowired
    private EquipamentoClientService equipamentoClient;

    public DevolucaoService(AluguelRepository aluguelRepository, CiclistaService ciclistaService, CiclistaRepository ciclistaRepository) {
        this.aluguelRepository = aluguelRepository;
        this.ciclistaService = ciclistaService;
        this.ciclistaRepository = ciclistaRepository;
    }

    public Aluguel devolverBicicleta(Long idCiclista, Long idBicicleta) {
        Aluguel aluguel = aluguelRepository.findByCiclistaIdAndBicicletaIdAndDataHoraFimIsNull(idCiclista, idBicicleta)
                .orElseThrow(() -> new IllegalArgumentException("Aluguel não encontrado."));

        aluguel.setHoraFim(String.valueOf(LocalDateTime.now()));
        int valor = calcularValor(aluguel.getHoraInicio(), aluguel.getHoraFim());
        aluguel.setCobranca(valor);
        aluguelRepository.save(aluguel);

/*        Bicicleta bicicleta = aluguel.getBicicleta();
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        bicicletaRepository.save(bicicleta);*/

        enviarEmailDevolucao();

        return aluguel;
    }

    private Integer calcularValor(String dataHoraInicio, String dataHoraFim) {
        LocalDateTime inicio = LocalDateTime.parse(dataHoraInicio);
        LocalDateTime fim = LocalDateTime.parse(dataHoraFim);
        Duration duration = Duration.between(inicio, fim);
        long minutos = duration.toMinutes();

        int valor = 10; // Valor base para as duas primeiras horas

        if (minutos > 120) {
            minutos -= 120;
            valor += (int) ((minutos / 30) * 5); // Valor adicional para cada meia hora adicional
        }

        return valor;
    }

    private void enviarEmailDevolucao() {
        //TODO: chamar microsservico Externo - tem o endpoint de envio de email
        System.out.println("Email de confirmação de devolução enviado com sucesso!");
    }
}