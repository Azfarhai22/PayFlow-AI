package com.payflow.identity.controller;

import com.payflow.identity.dto.LoginRequest;
import com.payflow.identity.dto.LoginResponse;
import com.payflow.identity.dto.RegisterRequest;
import com.payflow.identity.dto.RegisterResponse;
import com.payflow.identity.entity.User;
import com.payflow.identity.security.JwtService;
import com.payflow.identity.service.RefreshTokenService;
import com.payflow.identity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping ("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    public AuthController(UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody  RegisterRequest request) {
        User user = userService.registerUser(
            request.getFullName(),
            request.getEmail(), 
            request.getPassword()
        );

        RegisterResponse response = new RegisterResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request) {

            User user = userService.authenticateUser(
                request.getEmail(),
                request.getPassword()
            );

            String accessToken= jwtService.geneerateToken(
                    user.getId(),
                    user.getEmail()
            );

            String refreshToken = refreshTokenService
                    .createRefreshToken(user)
                    .getToken();

            LoginResponse response = new LoginResponse(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    accessToken,
                    refreshToken,
                    "Bearer"
            );



            return ResponseEntity.ok(response);
        }
        @GetMapping ("/profile")
        public ResponseEntity<String> getProfile() {
            // Implementation for fetching user profile
            return ResponseEntity.ok("JWT authentication successful! ");
        }
}
