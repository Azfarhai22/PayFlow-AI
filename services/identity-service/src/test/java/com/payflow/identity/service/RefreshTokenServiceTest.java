package com.payflow.identity.service;

import com.payflow.identity.entity.RefreshToken;
import com.payflow.identity.entity.User;
import com.payflow.identity.repository.RefreshTokenRepository;
import com.payflow.identity.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                refreshTokenService,"refreshTokenExpiration",604800000L
        );
    }
    @Test
    void shouldCreateRefreshToken() {

        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("test@example.com");

        String token = "sample-refresh-token";

        when(jwtService.generateRefreshToken(1L,"test@example.com"))
                .thenReturn(token);

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation->invocation.getArgument(0)
                );

        RefreshToken result = refreshTokenService.createRefreshToken(user);

        assertNotNull(result);
        assertEquals(token,result.getToken());
        assertEquals(user,result.getUser());
        assertFalse(result.isRevoked());
    }

    @Test
    void shouldValidateRefreshToken() {

        String token = "valid-refresh-token";

        RefreshToken refreshToken = new RefreshToken(
                token,
                LocalDateTime.now().plusDays(7),
                user
        );

        when(refreshTokenRepository.findByTokenAndRevokedFalse(token))
                .thenReturn(Optional.of(refreshToken));

        RefreshToken result = refreshTokenService.validateRefreshToken(token);

        assertNotNull(result);
        assertEquals(token,result.getToken());
        assertFalse(result.isRevoked());

    }
    @Test
    void shouldRejectExpiredRefreshToken() {
        String token = "expired-refresh-token";

        RefreshToken refreshToken = new RefreshToken(
                token,
                LocalDateTime.now().minusDays(1),
                user
        );
        when(refreshTokenRepository.findByTokenAndRevokedFalse(token))
        .thenReturn(Optional.of(refreshToken));

        assertThrows(RuntimeException.class,
                () -> refreshTokenService.validateRefreshToken(token)
        );

    }

    @Test
    void shouldRejectRevokedRefreshToken() {
        String token = "revoked-refresh-token";

        when(refreshTokenRepository.findByTokenAndRevokedFalse(token))
        .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> refreshTokenService.validateRefreshToken(token)
        );
    }

    @Test
    void shouldRevokeRefreshTokenSuccessfully() {
        String token = "refresh-token-to-revoke";

        RefreshToken refreshToken = new RefreshToken(
                token,
                LocalDateTime.now().plusDays(7),
                user
        );

        when(refreshTokenRepository.findByTokenAndRevokedFalse(token))
        .thenReturn(Optional.of(refreshToken));

        refreshTokenService.revokeRefreshToken(token);

        assertTrue(refreshToken.isRevoked());

        verify(refreshTokenRepository).save(refreshToken);
    }



}
