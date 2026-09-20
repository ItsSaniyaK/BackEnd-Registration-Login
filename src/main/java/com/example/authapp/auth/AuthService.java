package com.example.authapp.auth;

import com.example.authapp.auth.util.JwtUtil;
import com.example.authapp.exception.UnauthorizedException;
import com.example.authapp.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthRepo authRepo;
    private final JwtUtil jwtUtil;

    @Value("${app.jwt.cookie-name:jwt_token}")
    private String cookieName;

    @Value("${app.jwt.expiration-hours:24}")
    private long expirationHours;

    public AuthService(AuthRepo authRepo, JwtUtil jwtUtil) {
        this.authRepo = authRepo;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void issueToken(User user, HttpServletResponse response) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationHours * 3600_000L);
        String jwt = jwtUtil.generateToken(user.getName(), user.getId(), now, expiration);

        authRepo.save(new JwtToken(
                user.getId(),
                jwt,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(expirationHours)));

        response.addCookie(buildCookie(jwt, (int) (expirationHours * 3600)));
    }

    public String tokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Long resolveUid(String token) {
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Session expired or invalid token");
        }

        Number uidClaim = (Number) claims.get("uid");
        if (uidClaim == null) {
            throw new UnauthorizedException("Session expired or invalid token");
        }
        Long uid = uidClaim.longValue();

        JwtToken stored = authRepo.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Session expired or invalid token"));

        if (stored.getEndAt().isBefore(LocalDateTime.now())) {
            authRepo.deleteById(stored.getTid());
            throw new UnauthorizedException("Session expired");
        }
        if (!uid.equals(stored.getUid())) {
            throw new UnauthorizedException("Invalid token");
        }
        return uid;
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = tokenFromRequest(request);
        if (token != null) {
            try {
                Claims claims = jwtUtil.parseToken(token);
                Number uid = (Number) claims.get("uid");
                if (uid != null) {
                    authRepo.deleteByUidAndToken(uid.longValue(), token);
                }
            } catch (JwtException | IllegalArgumentException ignored) {
                // token already invalid/expired - nothing to revoke
            }
        }
        Cookie clearCookie = new Cookie(cookieName, null);
        clearCookie.setHttpOnly(true);
        clearCookie.setPath("/");
        clearCookie.setMaxAge(0);
        response.addCookie(clearCookie);
    }

    private Cookie buildCookie(String token, int maxAgeSeconds) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}