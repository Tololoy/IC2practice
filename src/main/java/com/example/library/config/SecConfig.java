package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers("/admin").hasRole("ADMIN");
            auth.requestMatchers("/user").hasRole("USER");
            auth.requestMatchers("/").permitAll();//la raiz es publica
            auth.anyRequest().authenticated();
        });
        http.httpBasic(Customizer.withDefaults());
        http.headers(header->header.frameOptions(Customizer.withDefaults()));
        return http.build();
    }
    @Bean
    public InMemoryUserDetailsManager userDetailsManagerService(){
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("1234"))
                .roles("USER").build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user,admin);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}
