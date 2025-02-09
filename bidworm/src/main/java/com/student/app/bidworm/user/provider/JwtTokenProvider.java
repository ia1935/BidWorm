package com.student.app.bidworm.user.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key}")
    private String secretKey; // Store this securely!

    @Value("${security.jwt.expiration-millis:3600000}") // Default to 1 hour if not set
    private long jwtExpirationMillis;

    /**
     * Generate a JWT token using the provided email as the subject.
     *
     * @param email the email to include in the token's claims.
     * @return the generated JWT token.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // The email is stored as the subject
                .setIssuedAt(new Date()) // Creation date
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis)) // Expiration
                .signWith(SignatureAlgorithm.HS512, secretKey) // Use HS512 algorithm with secret key
                .compact();
    }

    /**
     * Validate the provided JWT token.
     *
     * @param token the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            // Parse the token to ensure it's valid and correctly signed
            Jwts.parser()
                    .setSigningKey(secretKey) // Use the secret key to validate the signature
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log error if needed (e.g., expired, malformed, wrong signature)
            return false;
        }
    }

    /**
     * Extract the email (or subject) from the provided JWT token.
     *
     * @param token the JWT token.
     * @return the email stored in the token's claims (subject).
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // Use the secret key to parse claims
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody();
        return claims.getSubject(); // Retrieve the email stored in the subject
    }
}