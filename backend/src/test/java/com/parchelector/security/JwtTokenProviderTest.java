package com.parchelector.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for JwtTokenProvider.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenProvider Tests")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    // Base64 encoded secret key (minimum 256 bits for HS256)
    private static final String TEST_SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci1wYXJjaGUtbGVjdG9yLXVuaXQtdGVzdHMtb25seQ==";
    private static final long TEST_EXPIRATION_MS = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", TEST_EXPIRATION_MS);
    }

    @Nested
    @DisplayName("generateToken tests")
    class GenerateTokenTests {

        @Test
        @DisplayName("Should generate valid JWT token from authentication")
        void shouldGenerateTokenFromAuthentication() {
            // Arrange
            UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
            when(authentication.getPrincipal()).thenReturn(userDetails);

            // Act
            String token = jwtTokenProvider.generateToken(authentication);

            // Assert
            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
        }

        @Test
        @DisplayName("Should generate valid JWT token from username")
        void shouldGenerateTokenFromUsername() {
            // Arrange
            String username = "testuser";

            // Act
            String token = jwtTokenProvider.generateTokenFromUsername(username);

            // Assert
            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertTrue(token.split("\\.").length == 3);
        }
    }

    @Nested
    @DisplayName("getUsernameFromToken tests")
    class GetUsernameFromTokenTests {

        @Test
        @DisplayName("Should extract username from valid token")
        void shouldExtractUsernameFromToken() {
            // Arrange
            String username = "testuser";
            String token = jwtTokenProvider.generateTokenFromUsername(username);

            // Act
            String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

            // Assert
            assertEquals(username, extractedUsername);
        }

        @Test
        @DisplayName("Should extract correct username from authentication-generated token")
        void shouldExtractUsernameFromAuthToken() {
            // Arrange
            UserDetails userDetails = new User("authuser", "password", Collections.emptyList());
            when(authentication.getPrincipal()).thenReturn(userDetails);
            String token = jwtTokenProvider.generateToken(authentication);

            // Act
            String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

            // Assert
            assertEquals("authuser", extractedUsername);
        }
    }

    @Nested
    @DisplayName("validateToken tests")
    class ValidateTokenTests {

        @Test
        @DisplayName("Should return true for valid token")
        void shouldReturnTrueForValidToken() {
            // Arrange
            String token = jwtTokenProvider.generateTokenFromUsername("testuser");

            // Act
            boolean isValid = jwtTokenProvider.validateToken(token);

            // Assert
            assertTrue(isValid);
        }

        @Test
        @DisplayName("Should return false for malformed token")
        void shouldReturnFalseForMalformedToken() {
            // Arrange
            String malformedToken = "not.a.valid.jwt.token";

            // Act
            boolean isValid = jwtTokenProvider.validateToken(malformedToken);

            // Assert
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for empty token")
        void shouldReturnFalseForEmptyToken() {
            // Act
            boolean isValid = jwtTokenProvider.validateToken("");

            // Assert
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for expired token")
        void shouldReturnFalseForExpiredToken() {
            // Arrange - create provider with very short expiration
            JwtTokenProvider shortExpirationProvider = new JwtTokenProvider();
            ReflectionTestUtils.setField(shortExpirationProvider, "jwtSecret", TEST_SECRET);
            ReflectionTestUtils.setField(shortExpirationProvider, "jwtExpirationMs", 1L); // 1ms expiration

            String token = shortExpirationProvider.generateTokenFromUsername("testuser");

            // Wait for token to expire
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Act
            boolean isValid = shortExpirationProvider.validateToken(token);

            // Assert
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for token with invalid signature")
        void shouldReturnFalseForTokenWithInvalidSignature() {
            // Arrange - create token with different secret
            JwtTokenProvider otherProvider = new JwtTokenProvider();
            String differentSecret = "YW5vdGhlci1zZWNyZXQta2V5LWZvci10ZXN0aW5nLWludmFsaWQtc2lnbmF0dXJlcw==";
            ReflectionTestUtils.setField(otherProvider, "jwtSecret", differentSecret);
            ReflectionTestUtils.setField(otherProvider, "jwtExpirationMs", TEST_EXPIRATION_MS);

            String tokenWithDifferentSignature = otherProvider.generateTokenFromUsername("testuser");

            // Act - validate with original provider
            boolean isValid = jwtTokenProvider.validateToken(tokenWithDifferentSignature);

            // Assert
            assertFalse(isValid);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should complete full token lifecycle")
        void shouldCompleteFullTokenLifecycle() {
            // Arrange
            String username = "integrationuser";

            // Act - Generate token
            String token = jwtTokenProvider.generateTokenFromUsername(username);

            // Assert - Token is valid
            assertTrue(jwtTokenProvider.validateToken(token));

            // Assert - Username can be extracted
            assertEquals(username, jwtTokenProvider.getUsernameFromToken(token));
        }

        @Test
        @DisplayName("Should generate different tokens for different users")
        void shouldGenerateDifferentTokensForDifferentUsers() {
            // Arrange
            String user1 = "user1";
            String user2 = "user2";

            // Act
            String token1 = jwtTokenProvider.generateTokenFromUsername(user1);
            String token2 = jwtTokenProvider.generateTokenFromUsername(user2);

            // Assert
            assertNotEquals(token1, token2);
            assertEquals(user1, jwtTokenProvider.getUsernameFromToken(token1));
            assertEquals(user2, jwtTokenProvider.getUsernameFromToken(token2));
        }
    }
}

