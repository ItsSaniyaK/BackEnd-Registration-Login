package com.example.authapp.user;

import com.example.authapp.exception.BadRequestException;
import com.example.authapp.exception.UnauthorizedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(String name, String rawPassword, String email, String phoneNo) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Username is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new BadRequestException("Password is required");
        }
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (userRepo.existsByName(name)) {
            throw new BadRequestException("Username '" + name + "' is already taken");
        }
        String encoded = passwordEncoder.encode(rawPassword);
        return userRepo.save(new User(name.trim(), encoded, email.trim(), phoneNo));
    }

    @Transactional(readOnly = true)
    public User authenticate(String name, String rawPassword) {
        User user = userRepo.findByName(name)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
    }
}