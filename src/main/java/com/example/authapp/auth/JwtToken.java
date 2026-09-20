package com.example.authapp.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Lob
    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    public JwtToken() {
    }

    public JwtToken(Long uid, String token, LocalDateTime createAt, LocalDateTime endAt) {
        this.uid = uid;
        this.token = token;
        this.createAt = createAt;
        this.endAt = endAt;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}