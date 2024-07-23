/*
package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.application.external.EquipamentoService;
import com.example.bicicletario.bicicletario.domain.Aluguel;
import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final CiclistaService ciclistaService;
    private final CiclistaRepository ciclistaRepository;

    @Autowired
    private EquipamentoService equipamentoClient;

    public AluguelService(AluguelRepository aluguelRepository, CiclistaService ciclistaService, CiclistaRepository ciclistaRepository) {
        this.aluguelRepository = aluguelRepository;
        this.ciclistaService = ciclistaService;
        this.ciclistaRepository = ciclistaRepository;
    }

    public Aluguel alugarBicicleta(Long idCiclista, Long idBicicleta, String numeroTranca) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista)
                .orElseThrow(() -> new IllegalArgumentException("Ciclista não encontrado com o ID: " + idCiclista));

        if (!ciclistaService.permiteAluguel(ciclista.getId())) {
            throw new IllegalArgumentException("Ciclista não pode alugar uma bicicleta no momento.");
        }

        // Simulação de obtenção da bicicleta do serviço externo
        // Bicicleta bicicleta = bicicletaService.obterBicicleta(idBicicleta);

        // Simulação de validação da bicicleta
        if (bicicleta.getStatus() != StatusBicicleta.DISPONIVEL) {
            throw new IllegalArgumentException("Bicicleta não está disponível para aluguel.");
        }



        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclista.getId());
        // aluguel.setBicicleta(bicicleta);
        aluguel.setHoraInicio(String.valueOf(LocalDateTime.now()));
        aluguel.setTrancaInicio(Integer.valueOf(numeroTranca));
        aluguelRepository.save(aluguel);

        // Atualizar status da bicicleta no serviço externo
        // bicicletaService.atualizarStatusBicicleta(idBicicleta, StatusBicicleta.EM_USO);

        // Libera a tranca chamando o microserviço de Equipamento
        equipamentoClient.liberarTranca(Long.valueOf(numeroTranca), idBicicleta);


        enviarEmailAluguel(ciclista.getEmail(), aluguel);

        return aluguel;
    }

    private void enviarEmailAluguel(String email, Aluguel aluguel) {
        //TODO: chamar microsservico Externo - tem o endpoint de envio de email
        System.out.println("Email de confirmação de devolução enviado com sucesso!");
    }
}
*/
