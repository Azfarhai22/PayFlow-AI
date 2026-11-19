package com.payflow.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @NotBlank(message = "Full name is required")
    private String fullName;
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @NotBlank(message = "Email is required")
    @Email (message = "Please provide a valid email")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

 
}
