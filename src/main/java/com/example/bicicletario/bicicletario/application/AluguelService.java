package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.infraestructure.AluguelRepository;
import com.example.bicicletario.bicicletario.infraestructure.CiclistaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AluguelService {

    /*@Autowired
    private EquipamentoService equipamentoClient;*/


    /*public Aluguel alugarBicicleta(int idCiclista, int idBicicleta, String numeroTranca) {
        Ciclista ciclista = ciclistaRepository.findById(idCiclista)
                .orElseThrow(() -> new IllegalArgumentException("Ciclista não encontrado com o ID: " + idCiclista));

        if (!ciclistaService.permiteAluguel(ciclista.getId())) {
            throw new IllegalArgumentException("Ciclista não pode alugar uma bicicleta no momento.");
        }

        // Simulação de obtenção da bicicleta do serviço externo
        // Bicicleta bicicleta = bicicletaService.obterBicicleta(idBicicleta);

        // Simulação de validação da bicicleta
        *//*if (bicicleta.getStatus() != StatusBicicleta.DISPONIVEL) {
            throw new IllegalArgumentException("Bicicleta não está disponível para aluguel.");
        }*//*
        System.out.println("Bicicleta validada com sucesso!");


        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclista.getId());
        // aluguel.setBicicleta(bicicleta);
        aluguel.setHoraInicio(String.valueOf(LocalDateTime.now()));
        aluguel.setTrancaInicio(Integer.valueOf(numeroTranca));
        aluguelRepository.save(aluguel);

        // Atualizar status da bicicleta no serviço externo
        // bicicletaService.atualizarStatusBicicleta(idBicicleta, StatusBicicleta.EM_USO);

        // Libera a tranca chamando o microserviço de Equipamento
        //equipamentoClient.liberarTranca(Long.valueOf(numeroTranca), idBicicleta);
        System.out.println("Tranca liberada com sucesso!");


        enviarEmailAluguel(ciclista.getEmail(), aluguel);

        return aluguel;
    }*/

    /*private void enviarEmailAluguel(String email, Aluguel aluguel) {
        //TODO: chamar microsservico Externo - tem o endpoint de envio de email
        System.out.println("Email de confirmação de devolução enviado com sucesso!");
    }*/
}
