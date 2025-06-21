package com.example.library.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
