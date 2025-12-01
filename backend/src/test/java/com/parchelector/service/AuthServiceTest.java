package com.parchelector.service;

import com.parchelector.dto.request.LoginRequest;
import com.parchelector.dto.request.RegisterRequest;
import com.parchelector.dto.response.AuthResponse;
import com.parchelector.model.entity.User;
import com.parchelector.repository.PasswordResetTokenRepository;
import com.parchelector.repository.UserRepository;
import com.parchelector.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private IEmailService emailService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("encoded_password");
        testUser.setRole("USER");
        testUser.setActive(true);
    }

    @Nested
    @DisplayName("register tests")
    class RegisterTests {

        @Test
        @DisplayName("Should successfully register a new user")
        void shouldRegisterNewUser() {
            // Arrange
            RegisterRequest request = new RegisterRequest();
            request.setUsername("newuser");
            request.setEmail("new@example.com");
            request.setPassword("password123");

            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(tokenProvider.generateTokenFromUsername("newuser")).thenReturn("jwt_token");

            // Act
            AuthResponse response = authService.register(request);

            // Assert
            assertNotNull(response);
            assertEquals("jwt_token", response.getToken());
            assertEquals("newuser", response.getUsername());
            assertEquals("new@example.com", response.getEmail());
            assertEquals("USER", response.getRole());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        void shouldThrowExceptionWhenUsernameExists() {
            // Arrange
            RegisterRequest request = new RegisterRequest();
            request.setUsername("existinguser");
            request.setEmail("new@example.com");
            request.setPassword("password123");

            when(userRepository.existsByUsername("existinguser")).thenReturn(true);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> authService.register(request));
            assertEquals("Username already exists", exception.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Arrange
            RegisterRequest request = new RegisterRequest();
            request.setUsername("newuser");
            request.setEmail("existing@example.com");
            request.setPassword("password123");

            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> authService.register(request));
            assertEquals("Email already exists", exception.getMessage());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("login tests")
    class LoginTests {

        @Test
        @DisplayName("Should successfully login user")
        void shouldLoginUser() {
            // Arrange
            LoginRequest request = new LoginRequest();
            request.setUsernameOrEmail("testuser");
            request.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByUsernameOrEmail("testuser", "testuser"))
                    .thenReturn(Optional.of(testUser));
            when(tokenProvider.generateToken(authentication)).thenReturn("jwt_token");

            // Act
            AuthResponse response = authService.login(request);

            // Assert
            assertNotNull(response);
            assertEquals("jwt_token", response.getToken());
            assertEquals(1L, response.getUserId());
            assertEquals("testuser", response.getUsername());
            assertEquals("test@example.com", response.getEmail());
            assertEquals("USER", response.getRole());
        }

        @Test
        @DisplayName("Should login with email instead of username")
        void shouldLoginWithEmail() {
            // Arrange
            LoginRequest request = new LoginRequest();
            request.setUsernameOrEmail("test@example.com");
            request.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByUsernameOrEmail("test@example.com", "test@example.com"))
                    .thenReturn(Optional.of(testUser));
            when(tokenProvider.generateToken(authentication)).thenReturn("jwt_token");

            // Act
            AuthResponse response = authService.login(request);

            // Assert
            assertNotNull(response);
            assertEquals("testuser", response.getUsername());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            LoginRequest request = new LoginRequest();
            request.setUsernameOrEmail("nonexistent");
            request.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByUsernameOrEmail("nonexistent", "nonexistent"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> authService.login(request));
        }
    }

    @Nested
    @DisplayName("requestPasswordReset tests")
    class RequestPasswordResetTests {

        @Test
        @DisplayName("Should silently return when email does not exist")
        void shouldReturnSilentlyWhenEmailNotFound() {
            // Arrange
            when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

            // Act - should not throw
            assertDoesNotThrow(() -> authService.requestPasswordReset("nonexistent@example.com"));

            // Assert - no token should be created or email sent
            verify(tokenRepository, never()).save(any());
            verify(emailService, never()).sendPasswordResetEmail(any(), any(), any());
        }

        @Test
        @DisplayName("Should create token and send email for existing user")
        void shouldCreateTokenAndSendEmail() {
            // Arrange
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(passwordEncoder.encode(anyString())).thenReturn("encoded_token");

            // Act
            authService.requestPasswordReset("test@example.com");

            // Assert
            verify(tokenRepository).deleteByUserId(testUser.getId());
            verify(tokenRepository).save(any());
            verify(emailService).sendPasswordResetEmail(eq("test@example.com"), eq("testuser"), anyString());
        }
    }

    @Nested
    @DisplayName("Security and validation tests")
    class SecurityTests {

        @Test
        @DisplayName("Should encode password when registering")
        void shouldEncodePasswordWhenRegistering() {
            // Arrange
            RegisterRequest request = new RegisterRequest();
            request.setUsername("newuser");
            request.setEmail("new@example.com");
            request.setPassword("plainpassword");

            when(userRepository.existsByUsername(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode("plainpassword")).thenReturn("encoded_password");
            when(userRepository.save(any(User.class))).thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(1L);
                return u;
            });
            when(tokenProvider.generateTokenFromUsername(anyString())).thenReturn("token");

            // Act
            authService.register(request);

            // Assert
            verify(passwordEncoder).encode("plainpassword");
            verify(userRepository).save(argThat(user -> 
                "encoded_password".equals(user.getPasswordHash())
            ));
        }

        @Test
        @DisplayName("Should set correct default role when registering")
        void shouldSetDefaultRoleWhenRegistering() {
            // Arrange
            RegisterRequest request = new RegisterRequest();
            request.setUsername("newuser");
            request.setEmail("new@example.com");
            request.setPassword("password");

            when(userRepository.existsByUsername(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(1L);
                return u;
            });
            when(tokenProvider.generateTokenFromUsername(anyString())).thenReturn("token");

            // Act
            authService.register(request);

            // Assert
            verify(userRepository).save(argThat(user -> 
                "USER".equals(user.getRole()) && Boolean.TRUE.equals(user.getActive())
            ));
        }
    }
}

