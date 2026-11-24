package com.payflow.identity.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import com.payflow.identity.entity.User;
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
}
