package com.example.authapp.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-hours:24}")
    private long expirationHours;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Long uid, Date issuedAt, Date expiration) {
        return Jwts.builder()
                .subject(username)
                .claim("uid", uid)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public long getExpirationHours() {
        return expirationHours;
    }

    public Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}