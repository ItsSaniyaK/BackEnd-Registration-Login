package com.example.authapp.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String token);

    Optional<JwtToken> findByUidAndToken(Long uid, String token);

    void deleteByUidAndToken(Long uid, String token);
}