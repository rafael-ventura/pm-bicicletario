package com.example.bicicletario.bicicletario.web;

import com.example.bicicletario.bicicletario.application.HelloWorldService;
import com.example.bicicletario.bicicletario.domain.HelloWorld;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/hello-world")
    public HelloWorld getHelloWorld() {
        return helloWorldService.getHelloWorld();
    }
}



