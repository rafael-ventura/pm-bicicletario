package com.example.bicicletario.services;

import com.example.bicicletario.bicicletario.mapper.FuncionarioMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MapperIntegrationTest {

    @Autowired
    private FuncionarioMapper funcionarioMapper;

    @Test
    void testFuncionarioMapper() {
        assertNotNull(funcionarioMapper, "FuncionarioMapper deve ser injetado pelo Spring");
    }
}
