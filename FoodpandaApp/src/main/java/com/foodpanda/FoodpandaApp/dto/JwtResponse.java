package com.foodpanda.FoodpandaApp.dto;

import java.util.List;

public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, String username, List<String> roles) {
        this.token = accessToken;
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public List<String> getRoles() {
        return roles;
    }
}
