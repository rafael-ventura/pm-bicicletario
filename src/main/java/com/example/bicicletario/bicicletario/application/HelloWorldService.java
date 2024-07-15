package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.HelloWorld;
import com.example.bicicletario.bicicletario.infraestructure.HelloWorldRepository;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {
    public HelloWorld getHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setNome("Hello World !");
        return helloWorld;
    }
}
