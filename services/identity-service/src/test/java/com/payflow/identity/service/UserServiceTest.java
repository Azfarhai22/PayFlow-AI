package com.payflow.identity.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.payflow.identity.entity.User;
import com.payflow.identity.exception.EmailAlreadyExistsException;
import com.payflow.identity.exception.InvalidCredentialsException;
import com.payflow.identity.repository.UserRepository;



    @ExtendWith (MockitoExtension.class)
    class UserServiceTest {
      
        @Mock
        private UserRepository userRepository;

       @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private UserService userService;

        @Test 
        void shouldRegisterUserSuccessfully() {
            // 1. Mock setup
            // Test implementation for successful user registration
           when(passwordEncoder.encode("password123"))
           .thenReturn("encodedPassword");

            when(userRepository.save(any(User.class))).
            thenAnswer(invocation -> invocation.getArgument(0));

            // 2. Actual method call
            User savedUser = userService.registerUser(
                "Azfar",
                "azfar@example.com",
                "password123"
            );

            // 3. Assertions
            assertNotNull(savedUser);
            assertEquals("Azfar", savedUser.getFullName());
            assertEquals("azfar@example.com", savedUser.getEmail());
            assertEquals("encodedPassword", savedUser.getPassword());

            // 4. Verify
            verify(userRepository).save(any(User.class));
            verify(passwordEncoder).encode("password123");
            
        }

        @Test 
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // 1. Mock setup
            when(userRepository.existsByEmail("azfar@example.com")).
            thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> {
                // 2. Actual method call
                userService.registerUser(
                    "Azfar",
                    "azfar@example.com",
                    "password123"
                );
            });
            verify(userRepository, never()).save(any(User.class));

        }

        @Test 
        void shouldAuthenticateUserSuccessfully() {
            // 1. Mock setup
            User user = new User(
                "Azfar",
                "azfar@example.com",
                "encodedPassword"
            );
            when(userRepository.findByEmail("azfar@example.com"))
            .thenReturn(Optional.of(user));
            
            when(passwordEncoder.matches("password123", "encodedPassword"))
            .thenReturn(true);
           
        // 2. Actual method call
        User authenticatedUser = userService.authenticateUser(
            "azfar@example.com",
            "password123"
        );

        assertNotNull(authenticatedUser);
        assertEquals("Azfar", authenticatedUser.getFullName());
        assertEquals("azfar@example.com", authenticatedUser.getEmail());

        verify(userRepository).findByEmail("azfar@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test 
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // 1. Mock setup
        User user = new User(
            "Azfar",
            "azfar@example.com",
            "encodedPassword"
        );
        when(userRepository.findByEmail("azfar@example.com"))
        .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password123", "encodedPassword"))
        .thenReturn(false);

        // 2. Actual method call
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.authenticateUser(
                "azfar@example.com",
                "password123"
            );
        });

        verify(userRepository).findByEmail("azfar@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test 
    void shouldThrowExceptionWhenEmailDoesNotExist() {
        // 1. Mock setup
        when(userRepository.findByEmail("azfar@example.com"))
        .thenReturn(Optional.empty());

        // 2. Actual method call
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.authenticateUser(
                "azfar@example.com",
                "password123"
            );
        });

        verify(userRepository).findByEmail("azfar@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}