package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Bicicleta;
import com.example.bicicletario.bicicletario.domain.Tranca;
import com.example.bicicletario.bicicletario.domain.enums.Status;
import com.example.bicicletario.bicicletario.dto.IntegrarNaRedeDTO;
import com.example.bicicletario.bicicletario.dto.RetirarDaRedeDTO;
import com.example.bicicletario.bicicletario.infraestructure.BicicletaRepository;
import com.example.bicicletario.bicicletario.infraestructure.TrancaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final TrancaRepository trancaRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.trancaRepository = trancaRepository;
    }


    /*
     * @return List<Bicicleta>
     *
     */
    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    /*
     * @param bicicleta
     * @return bicicleta
     * */
    public Bicicleta criarBicicleta(Bicicleta bicicleta) {
        //TODO: mockar com mockito
        return bicicletaRepository.save(bicicleta);
    }

    public void integrarNaRede(IntegrarNaRedeDTO dto) {
        // 1. O sistema lê o número da bicicleta.
        Optional<Bicicleta> bicicletaOpt = bicicletaRepository.findById(dto.getIdBicicleta());
        if (bicicletaOpt.isEmpty()) {
            // [E1] Número da bicicleta inválido.
            throw new IllegalArgumentException("Número da bicicleta inválido");
        }

        Bicicleta bicicleta = bicicletaOpt.get();

        // 2. O sistema valida o número da bicicleta [E1][E3][R3].
        if (bicicleta.getStatus() != Status.NOVA && bicicleta.getStatus() != Status.EM_REPARO) {
            throw new IllegalArgumentException("Status da bicicleta inválido");
        }

        // Verifica se a tranca está disponível
        if (!isTrancaDisponivel(dto.getIdTranca())) {
            throw new IllegalArgumentException("Tranca não está disponível");
        }

        // Verifica se o funcionário que está devolvendo é o mesmo que retirou para reparo (se aplicável)
        if (bicicleta.getStatus() == Status.EM_REPARO && !isFuncionarioValido(dto.getIdFuncionario(), bicicleta.getIdFuncionarioReparador())) {
            throw new IllegalArgumentException("Funcionário inválido para esta operação");
        }

        // 3. O sistema registra os dados da inclusão da bicicleta [R1].
        // 5. O sistema altera o status da bicicleta para “disponível”.
        bicicleta.setStatus(Status.DISPONIVEL);
        bicicleta.setDataInsercaoTranca(LocalDateTime.now().toString()); //TODO: revisar tipo do campo na entidade
        bicicletaRepository.save(bicicleta);

        // 4. O sistema solicita o fechamento da tranca.
        trancaRepository.findById(dto.getIdTranca()).ifPresent(tranca -> {
            tranca.setStatus(Status.EM_USO);
            trancaRepository.save(tranca);

            // 6. O sistema envia uma mensagem para o reparador informando os dados da inclusão da bicicleta [R2] [E2].
            try {
                //enviarEmailReparador(bicicleta, dto.getIdFuncionario());
            } catch (Exception e) {
                throw new IllegalArgumentException("Erro no envio do email");
            }

            // 7. O sistema informa que a bicicleta foi incluída com sucesso.
            // (A resposta será gerenciada pelo controlador)
        });
    }

    public void retirarDaRede(RetirarDaRedeDTO dto) {
        // Lógica para retirar a bicicleta da rede
        // Implementar conforme necessário, seguindo lógica semelhante ao método acima
    }

    private boolean isFuncionarioValido(Long idFuncionario, Long idFuncionarioReparador) {
        // Verifica se o funcionário é o mesmo que retirou para reparo
        return idFuncionario.equals(idFuncionarioReparador);
    }

    private void enviarEmailReparador(Bicicleta bicicleta, Long idFuncionario) throws Exception {
        // Lógica para enviar email ao reparador
        // Substituir com a lógica real de envio de email
    }

    private boolean isTrancaDisponivel(Long idTranca) {
        // Lógica para verificar se a tranca está disponível
        // Substituir com a lógica real
        Tranca tranca = trancaRepository.findById(idTranca).get();
        return tranca.getStatus() == Status.DISPONIVEL;
    }
}
