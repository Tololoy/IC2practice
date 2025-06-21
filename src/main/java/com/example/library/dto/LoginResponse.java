package com.example.library.dto;

import java.util.List;

public class LoginResponse {
    private String username;
    private List<String> roles;
    private String token;

    public LoginResponse(String username,List<String> roles, String token){
        this.username=username;
        this.roles=roles;
        this.token=token;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getToken() {
        return token;
    }
}
