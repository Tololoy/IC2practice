package com.example.library.config;

import com.example.library.model.Libro;
import com.example.library.model.Usuario;
import com.example.library.repository.LibroRepository;
import com.example.library.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initUsers(UsuarioRepository repo, PasswordEncoder encoder){
        return args -> {
            if(repo.count()==0){
                repo.save(new Usuario("admin", encoder.encode("1234"), "ROLE_ADMIN"));
                repo.save(new Usuario("user", encoder.encode("1234"), "ROLE_USER"));
            }
        };
    }

    @Bean
    public CommandLineRunner cargarLibros(LibroRepository repo) {
        return args -> {
            repo.save(new Libro(null, "1984", "George Orwell", "Distopía", 1949, true));
            repo.save(new Libro(null, "El Principito", "Antoine de Saint-Exupéry", "Fábula", 1943, true));
        };
    }


}
