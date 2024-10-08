package com.taskease.taskeasebackend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${security.jwt.token.secret-key}")
    private String SECRET_KEY;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // Set the username as the subject
                .setIssuedAt(new Date())  // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Set expiration time (10 hours)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Sign the token
                .compact();  // Compact the JWT into a string
    }
}
