package com.parchelector.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Mock Email Service for testing without real email.
 * Active only in 'mock' profile.
 * Logs password reset information to console instead of sending emails.
 * 
 * @author Nicolas Arciniegas
 */
@Service
@Profile("mock")
public class MockEmailService implements IEmailService {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Mock sending password reset email - just logs to console.
     */
    public void sendPasswordResetEmail(String toEmail, String username, String token) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🔒 PASSWORD RESET EMAIL (MOCK)                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║  To: " + toEmail);
        System.out.println("║  Username: " + username);
        System.out.println("║");
        System.out.println("║  Reset Link:");
        System.out.println("║  " + frontendUrl + "/reset-password?token=" + token);
        System.out.println("║");
        System.out.println("║  Token: " + token);
        System.out.println("║");
        System.out.println("║  This link expires in 1 hour.");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
}
