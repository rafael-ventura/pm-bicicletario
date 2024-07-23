package com.example.bicicletario.bicicletario.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SwaggerController {

    @GetMapping("/swagger")
    public String swagger() {
        return "swagger"; // Retorna o arquivo swagger.html
    }

    @GetMapping("/")
    public String index() {
        return "swagger"; // Retorna o arquivo swagger.html
    }
}
