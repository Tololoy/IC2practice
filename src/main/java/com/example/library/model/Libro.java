package com.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo no puede estar en blanco")
    @Column(nullable = false,length = 50)
    private String titulo;

    @Column(nullable = false, length = 30)
    private String autor;

    @Column(nullable = false,length = 30)
    private String genero;

    @NotNull(message = "El anio no puede ser nulo")
    @Min(value = 1500,message = "El anio debe ser mayor o igual a 1500")
    @Max(value = 2025,message = "El anio debe ser menor o igual a 2025")
    @Column(nullable=false,length = 4)
    private Integer anio;

    @Column(nullable = false)
    private Boolean disponible;

    public Libro(){}

    public Libro(Long id, String titulo, String autor, String genero, Integer anio, Boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anio = anio;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", genero='" + genero + '\'' +
                ", anio=" + anio +
                ", disponible=" + disponible +
                '}';
    }

}
