package com.example.library.controller;

import com.example.library.model.Libro;
import com.example.library.service.LibroService;
import jakarta.validation.Valid;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    public static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    @Autowired
    LibroService service;

    @GetMapping
    @Cacheable("libros")
    public ResponseEntity<List<Libro>> obtenerTodos(){
        List<Libro> libros=service.obtenerTodos();
        return ResponseEntity.ok(libros);//200 OK
    }
    @GetMapping("/por-genero")
    public ResponseEntity<Map<String,List<Libro>>> librosPorGenero(){
        Map<String,List<Libro>> agrupados = service.agruparPorGenero();
        return agrupados.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(agrupados);
    }
    @GetMapping("/stats/promedio-anio")
    public ResponseEntity<Double> promedio(){
        double promedio = service.calcularPromedioAnio();
        return promedio == 0.0
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(promedio);
    }
    @GetMapping("/autores")
    public ResponseEntity<List<String>> autores(){
        List<String> autores=service.obtenerAutoresOrdenados();
        return autores.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(autores);
    }
    @GetMapping("/generos")
    public ResponseEntity<List<String>> generos(){
        List<String> generos = service.obtenerGenerosUnicos();
        return generos.isEmpty()
                ?ResponseEntity.noContent().build()
                :ResponseEntity.ok(generos);
    }
    @Cacheable(value = "libroPorId",key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id){
        logger.info("GET /api/libros/{}",id);
         Libro libro= service.obtenerPorId(id);
         return ResponseEntity.ok(libro);
    }
    @GetMapping("/filtro")
    public ResponseEntity<Page<Libro>> filtrado(
            @RequestParam(defaultValue="") String titulo,
            @RequestParam(defaultValue = "") String autor,
            Pageable pageable
    ){
        Page<Libro> librosFiltrados =service.filtrar(titulo,autor,pageable);
        return ResponseEntity.ok(librosFiltrados);
    }
    @PostMapping
    @CacheEvict(value = "libros",allEntries = true)
    public ResponseEntity<Libro> agregar(@RequestBody @Valid Libro libro){
        Libro l= service.agregar(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(l);
    }
    @PutMapping("/{id}")
    @Caching(evict = {
            @CacheEvict(value = "libros",allEntries = true),
            @CacheEvict(value = "libroPorId",key = "#id")
    })
    public ResponseEntity<Libro> actualizar(@RequestBody @Valid Libro libro,@PathVariable Long id){
        Libro actualizado=service.actualizar(id,libro);
        return ResponseEntity.ok(actualizado);
    }
    @DeleteMapping("/{id}")
    @Caching(evict = {
            @CacheEvict(value = "libros",allEntries = true),
            @CacheEvict(value = "libroPorId",key = "#id")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build();//204 No Content
    }

}
