package com.example.authapp.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);
}