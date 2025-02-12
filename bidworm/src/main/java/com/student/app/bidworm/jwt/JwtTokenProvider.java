package com.student.app.bidworm.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return token;
    }


    /**
     * Validate the provided JWT token.
     *
     * @param token the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {

//            System.out.println("Inside ValidateToken with token: "+ token);
            Jwts.parser()
                    .setSigningKey(secretKey) // Use the secret key to validate the signature
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException ex) {
            // Token signature validation failed
            System.err.println("Invalid JWT signature: " + ex.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            // Token is expired
            System.err.println("Expired JWT token: " + ex.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            // Token format is invalid
            System.err.println("Malformed JWT token: " + ex.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException ex) {
            // Token is unsupported
            System.err.println("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // Token is null or empty
            System.err.println("JWT claims string is empty: " + ex.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        try {
//            System.out.println("Inside getEmailFromToken with token: "+ token);
//            System.out.println("Using secret key: " + secretKey);

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
//            System.out.println("Email: " + claims.getSubject());
            return claims.getSubject(); // Retrieve email from the subject
        } catch (Exception ex) {
            System.err.println("Failed to extract email from token: " + ex.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public String refreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            // Generate a new token with updated expiration
            return generateToken(email);
        } catch (ExpiredJwtException ex) {
            // Extract email from expired token and generate a new one
            String email = ex.getClaims().getSubject();
            return generateToken(email);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

}