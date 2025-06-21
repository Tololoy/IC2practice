package com.example.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Campo requerido!")
    private String username;
    @NotEmpty(message = "Campo requerido!")
    private String password;
    @NotEmpty(message = "Campo requerido!")
    private String rol;

    public Usuario() {}

    public Usuario(Long id, String username, String password, String rol) {;
        this.id=id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String username, String password, String rol) {;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
