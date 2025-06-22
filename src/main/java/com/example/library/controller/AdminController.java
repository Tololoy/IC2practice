package com.example.library.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class AdminController {
    @GetMapping("/admin")
    public String soloAdmin() {
        return "Bienvenido, administrador";
    }
    @GetMapping("/user")
    public String soloUser() {
        return "Bienvenido, usuario";
    }
}
