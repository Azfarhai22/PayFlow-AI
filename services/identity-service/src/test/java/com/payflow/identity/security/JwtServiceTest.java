package com.payflow.identity.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

    class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        jwtService = new JwtService();

        String testSecret = java.util.Base64.getEncoder()
                .encodeToString(
                        "01234567890123456789012345678901".getBytes()
                );

        ReflectionTestUtils.setField(jwtService, "secretKey", testSecret);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 900000L);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", 604800000L);
    }

    @Test
    void shouldGenerateAccessTokenSuccessfully() {

        String token = jwtService.geneerateToken(
                1L,
                "azfar@example.com"
        );
        assertNotNull(token);
        assertFalse(token.isBlank());

        String email = jwtService.extractEmail(token);

        assertEquals("azfar@example.com", email);
    }
    @Test
    void shouldGenerateRefreshTokenSuccessfully() {
        String token = jwtService.generateRefreshToken(
                1L,
                "azfar@example.com"
        );

        assertNotNull(token);
        assertFalse(token.isBlank());

        assertTrue(jwtService.isRefreshToken(token));

        String email = jwtService.extractEmail(token);

        assertEquals("azfar@example.com", email);
    }
    @Test
    void shouldReturnFalseWhenAccessTokenIsCheckedAsRefreshToken() {
        String token = jwtService.geneerateToken(
                1L,
                "azfar@example.com"
        );
        assertFalse(jwtService.isRefreshToken(token));
    }
}
