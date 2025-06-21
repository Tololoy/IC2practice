package com.example.library.service;

import com.example.library.exception.LibroNoEncontradoException;
import com.example.library.model.Libro;
import com.example.library.repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @InjectMocks
    private LibroService service;

    @Test
    public void testActualizarLibro_IdNoExiste_LanzaExcepcion(){
        Long idInexistente=99L;

        Libro libroActualizado = new Libro();
        libroActualizado.setTitulo("Nuevo título");
        libroActualizado.setAutor("Nuevo autor");
        libroActualizado.setAnio(2022);
        libroActualizado.setDisponible(true);
        libroActualizado.setGenero("Ficción");

        //Mock: cuando se busca el id, retorna vacio
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());
        //Act+Assert
        LibroNoEncontradoException exception = org.junit.jupiter.api.Assertions.assertThrows(
                LibroNoEncontradoException.class,
                () -> service.actualizar(idInexistente, libroActualizado)
        );
        assertEquals("No se puede actualizar porque no existe libro con id"+idInexistente,exception.getMessage());
        //verifica que nunca se llamo a save
        verify(repository).findById(idInexistente);
        verify(repository).save(any(Libro.class));
    }

    @Test
    public void testActualizarLibro_Exitoso(){
        Long id=1L;

        //libro simulado en la bd
        Libro libroExistente = new Libro();
        libroExistente.setId(id);
        libroExistente.setTitulo("Antiguo");
        libroExistente.setAutor("Autor viejo");
        libroExistente.setAnio(2000);
        libroExistente.setDisponible(false);
        libroExistente.setGenero("Drama");

        // Datos nuevos que vienen del cliente
        Libro libroActualizado = new Libro();
        libroActualizado.setTitulo("Nuevo título");
        libroActualizado.setAutor("Nuevo autor");
        libroActualizado.setAnio(2022);
        libroActualizado.setDisponible(true);
        libroActualizado.setGenero("Ficción");

        //Mock: Cuando busquemos por id, retorna el existente
        when(repository.findById(id)).thenReturn(Optional.of(libroExistente));
        //Mock: Cuando se guarda, retorna el mismo objeto
        when(repository.save(any(Libro.class))).thenReturn(libroExistente);
        //Act
        Libro resultado=service.actualizar(id,libroActualizado);
        //Assert
        assertEquals("Nuevo título",libroActualizado.getTitulo());
        assertEquals("Nuevo autor",libroActualizado.getAutor());
        assertEquals((Integer)2022,resultado.getAnio());
        assertTrue(resultado.getDisponible());

        verify(repository).save(libroExistente);
    }
    @Test
    public void testObtenerPorId_ExisteLibro_RetornaLibro(){
        Long id=1L;

        Libro libro = new Libro();
        libro.setId(id);
        libro.setTitulo("Clean code");
        libro.setAutor("Robert C. Martin");
        libro.setGenero("Programación");
        libro.setAnio(2008);
        libro.setDisponible(true);

        when(repository.findById(id)).thenReturn(Optional.of(libro));
        //Act
        Libro resultado = service.obtenerPorId(id);
        //Assert
        assertEquals(id,resultado.getId());
        assertEquals("Clean code",resultado.getTitulo());
        verify(repository).findById(id);
    }
    @Test
    public void testEliminarLibro_Existe_RetornaOk(){
        Long id=1L;
        Libro libro = new Libro();
        libro.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(libro));

        //Act
        service.eliminar(id);

        //Assert
        verify(repository).findById(id);
        verify(repository).deleteById(id);
    }
    @Test
    public void testEliminarLibro_NoExiste_LanzaExcepcion(){
        Long id=99L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        //Act + Assert
        LibroNoEncontradoException exception = assertThrows(
                LibroNoEncontradoException.class,
                () -> service.eliminar(id)
        );
        assertEquals("Id inexistente, 0 elementos borrados.", exception.getMessage());
        verify(repository).findById(id);
        verify(repository,org.mockito.Mockito.never()).deleteById(id);
    }
}
/*
* ¿Qué hace any(Libro.class) en when(...).thenReturn(...)?
La función any(Class<T> type) de Mockito es un argument matcher que le dice a Mockito:

“Cuando se llame a este método, no me importa qué objeto del tipo Libro se le pase exactamente, acepta cualquiera de ese tipo”.
* */

/*
* 🛡️ ¿Y si quiero ser más preciso?
Si quisieras verificar el contenido del Libro pasado a save(), puedes usar:


verify(repository).save(argThat(libro ->
    libro.getTitulo().equals("Nuevo título") &&
    libro.getAutor().equals("Nuevo autor")
));
* */