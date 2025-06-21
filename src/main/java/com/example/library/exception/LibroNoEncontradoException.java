package com.example.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LibroNoEncontradoException extends RuntimeException{
    public LibroNoEncontradoException(String m){
        super(m);
    }
    public LibroNoEncontradoException(String m, Throwable causa){
        super(m,causa);
    }

}
