package com.example.library.service;

import com.example.library.model.Libro;
import com.example.library.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.library.exception.LibroNoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibroService {
    @Autowired
    LibroRepository repo;

    public static final Logger logger = LoggerFactory.getLogger(LibroService.class);

    public List<Libro> obtenerTodos(){
        return repo.findAll();
    }
    public Libro obtenerPorId(Long id){
        Optional<Libro> libroOpt = repo.findById(id);
        return  libroOpt.orElseThrow(()->new LibroNoEncontradoException("Libro inexistente con id "+id));
    }
    public Libro agregar(Libro libro){
        return repo.save(libro);
    }
    public Libro actualizar(Long id,Libro updatedBook){
        Libro libro = repo.findById(id)
                        .orElseThrow(()->new LibroNoEncontradoException("No se puede actualizar porque no existe libro con id"+id));
        libro.setAnio(updatedBook.getAnio());
        libro.setAutor(updatedBook.getAutor());
        libro.setDisponible(updatedBook.getDisponible());
        libro.setTitulo(updatedBook.getTitulo());
        libro.setGenero(updatedBook.getGenero());
        return repo.save(libro);
    }
    public void eliminar(Long id){
        Optional<Libro> existeOpt=repo.findById(id);
        if (existeOpt.isEmpty()){
            logger.error("Libro No eliminado con id inexistente: ",id);
            throw new LibroNoEncontradoException("Id inexistente, 0 elementos borrados.");
        }
        repo.deleteById(id);

    }
    public List<String> obtenerAutoresOrdenados(){
        logger.info("Obteniendo autores ordenados por orden alfabetico.");
        return repo.findAll().stream()
                .map(Libro::getAutor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    public double calcularPromedioAnio(){
        List<Libro> libros = repo.findAll();
        OptionalDouble promedio = libros.stream()
                .mapToInt(Libro::getAnio)
                .average();
        return promedio.orElse(0.0);
    }
    public List<String> obtenerGenerosUnicos(){
        return repo.findAll().stream()
                .map(Libro::getGenero)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
    public Map<String,List<Libro>> agruparPorGenero(){
        return repo.findAll().stream()
                .filter(libro -> libro.getGenero()!=null)
                .collect(Collectors.groupingBy(Libro::getGenero));
    }
    public Page<Libro> filtrar(String titulo, String autor, Pageable pageable){
        return repo.findByTituloContainingIgnoreCaseAndAutorContainingIgnoreCase(titulo,autor,pageable);
    }

}
