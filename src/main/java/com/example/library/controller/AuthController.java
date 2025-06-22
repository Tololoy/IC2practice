package com.example.library.controller;

import com.example.library.dto.LoginRequest;
import com.example.library.dto.LoginResponse;
import com.example.library.repository.UsuarioRepository;
import com.example.library.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );//Extraer roles
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            // Generar token
            String token = jwtUtil.generateToken(request.getUsername(),roles);
            String refreshToken= jwtUtil.generateRefreshToken(request.getUsername());
            //Devolver respuesta
            return ResponseEntity.ok(new LoginResponse(request.getUsername(),roles,token,refreshToken));
        }catch (AuthenticationException e){
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String,String> body){
        String refreshToken=body.get("refreshToken");
        if(!jwtUtil.validateToken(refreshToken)){
            return ResponseEntity.status(401).body("Invalid Refresh Token");
        }
        String username=jwtUtil.getUsernameFromToken(refreshToken);
        List<String> roles=usuarioRepository.findByUsername(username)
                .map(u->List.of(u.getRol()))
                .orElse(List.of());
        String newAccessToken=jwtUtil.generateToken(username,roles);
        return ResponseEntity.ok(Map.of("accessToken",newAccessToken));
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            Map<String,Object> response=new HashMap<>();
            response.put("username",auth.getName());
            response.put("roles",auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray());

            return ResponseEntity.ok(response);
        }catch (AuthenticationException e){
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }
    }*/
}
