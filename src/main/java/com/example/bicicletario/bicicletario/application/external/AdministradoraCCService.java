package com.example.bicicletario.bicicletario.application.external;

import com.example.bicicletario.bicicletario.domain.dto.NovoCartaoDeCreditoDTO;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.exception.BadRequestException;
import com.example.bicicletario.bicicletario.exception.InvalidDataException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class AdministradoraCCService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdministradoraCCService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${externo.base-url}")
    private String baseUrl;

    public void validarCartao(NovoCartaoDeCreditoDTO cartaoDeCreditoDTO) {
        String url = baseUrl + "/validaCartaoDeCredito";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NovoCartaoDeCreditoDTO> request = new HttpEntity<>(cartaoDeCreditoDTO, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            logger.error("Erro ao validar o cartão de crédito: {}", cartaoDeCreditoDTO.getNumero(), e);
            throw new InvalidDataException("Erro ao validar o cartão de crédito.");
        }
    }

    public boolean enviarCobranca(NovoCobrancaDTO novoCobrancaDTO) {
        String url = baseUrl + "/cobranca";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NovoCobrancaDTO> request = new HttpEntity<>(novoCobrancaDTO, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.error("Erro ao processar pagamento", e);
            throw new BadRequestException("Erro ao processar pagamento.");
        }
    }

    public void registrarCobrancaPendente(int idCiclista) {
        logger.info("Registrando cobrança pendente para o ciclista {}", idCiclista);

        String url = baseUrl + "/filaCobranca";

        NovoCobrancaDTO cobrancaDTO = new NovoCobrancaDTO();
        cobrancaDTO.setCiclista(idCiclista);
        cobrancaDTO.setValor(10.0); // Set a default value or retrieve it from another source

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NovoCobrancaDTO> request = new HttpEntity<>(cobrancaDTO, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Cobrança pendente registrada com sucesso para o ciclista {}", idCiclista);
            } else {
                logger.error("Erro ao registrar cobrança pendente para o ciclista {}", idCiclista);
                throw new BadRequestException("Erro ao registrar cobrança pendente.");
            }
        } catch (Exception e) {
            logger.error("Erro ao registrar cobrança pendente", e);
            throw new BadRequestException("Erro ao registrar cobrança pendente.");
        }
    }
}
