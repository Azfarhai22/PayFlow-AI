package com.payflow.identity.dto;

public class LoginResponse {

    private Long id;
    private String fullName;
    private String email;
    private String accessToken;
    private String tokenType;

    public LoginResponse(Long id, String fullName, String email, String accessToken, String tokenType) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    
}
