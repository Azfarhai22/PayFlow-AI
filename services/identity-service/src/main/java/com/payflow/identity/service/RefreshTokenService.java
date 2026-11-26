package com.payflow.identity.service;

import com.payflow.identity.entity.RefreshToken;
import com.payflow.identity.entity.User;
import com.payflow.identity.repository.RefreshTokenRepository;
import com.payflow.identity.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        String token = jwtService.generateRefreshToken(
                user.getId(),
                user.getEmail()
        );

        RefreshToken refreshToken = new RefreshToken(
                token,
                LocalDateTime.now().plus(
                        Duration.ofMillis(refreshTokenExpiration)
                ),
                user
        );
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenAndRevokedFalse(token)
                .orElseThrow(()->
                        new RuntimeException("Refresh token not found or revoked")
                );

        if(!refreshToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }
        return refreshToken;
    }

    @Transactional
    public void revokeRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenAndRevokedFalse(token)
                .orElseThrow(()->
                        new RuntimeException("Refresh token not found or already revoked")
                );

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }

}
