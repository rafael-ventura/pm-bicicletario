package com.example.bicicletario.bicicletario.application.services;

import com.example.bicicletario.bicicletario.application.exceptions.ResourceNotFoundException;
import com.example.bicicletario.bicicletario.domain.models.Funcionario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class FuncionarioService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Funcionario get(Integer idFuncionario) {
        String baseUrl = "http://ec2-3-91-187-43.compute-1.amazonaws.com:8040/api";
        String url = baseUrl + "/funcionario/" + idFuncionario;

        try {
            ResponseEntity<Funcionario> response = restTemplate.getForEntity(url, Funcionario.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new ResourceNotFoundException("Funcionário não encontrado");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Funcionário não encontrado");
            } else {
                throw new ResourceNotFoundException("Erro ao buscar funcionário: " + e.getMessage());
            }
        }
    }

    public boolean isFuncionarioValido(Integer idFuncionario) {
        Funcionario funcionario = get(idFuncionario);
        return funcionario != null && funcionario.getId().equals(idFuncionario);
    }
}
