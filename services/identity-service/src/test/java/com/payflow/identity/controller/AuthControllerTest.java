package com.payflow.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;


import org.junit.jupiter.api.Test;
import com.payflow.identity.controller.AuthController;
import com.payflow.identity.entity.User;
import com.payflow.identity.security.JwtService;
import com.payflow.identity.security.SecurityConfig;
import com.payflow.identity.service.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest (AuthController.class)
@Import (SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private UserService userService;

    @MockitoBean 
    private JwtService jwtService;

    @Test 
    void shouldRegisterUserSuccessfully() throws Exception {
        // Test implementation for successful user registration

        User user = new User(
            "Azfar",
            "azfar@example.com",
            "encodedPassword"
        );
        user.setId(1L);
        user.setCreatedAt(LocalDateTime.of(2026,11,24,10,0));
        
        when(userService.registerUser(
            "Azfar",
            "azfar@example.com",
            "password123"
        )).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                "fullName": "Azfar",
                "email": "azfar@example.com",
                "password": "password123"
            }""")
        )
        
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.fullName").value("Azfar"))
        .andExpect(jsonPath("$.email").value("azfar@example.com"));

        verify(userService).registerUser(
            "Azfar",
            "azfar@example.com",
            "password123"
        );

    }

    @Test 
    void shouldLoginUserSuccessfully() throws Exception {
        User user =new User(
            "Azfar",
            "azfar@example.com",
            "encodedPassword"
        );
        user.setId(1L);

        when(userService.authenticateUser(
           "azfar@example.com",
           "password123"
         )).thenReturn(user);

         when(jwtService.geneerateToken(1L, 
            "azfar@example.com"
         )).thenReturn("mock-jwt-taken");

         mockMvc.perform(post("/api/v1/auth/login")
           .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { 
                   "email":"azfar@example.com",
                   "password":"password123"


                }"""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.fullName").value("Azfar"))
            .andExpect(jsonPath("$.email").value("azfar@example.com"))
            .andExpect(jsonPath("$.accessToken").value("mock-jwt-taken"))
            .andExpect(jsonPath("$.tokenType").value("Bearer"));

        verify(userService).authenticateUser(
            "azfar@example.com",
            "password123"
        );

        verify(jwtService).geneerateToken(
            1L,
            "azfar@example.com" 
        );

           
    }
    
    
    
}
