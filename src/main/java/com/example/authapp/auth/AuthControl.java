package com.example.authapp.auth;

import com.example.authapp.dto.ApiResponse;
import com.example.authapp.dto.RegistrationRequest;
import com.example.authapp.user.User;
import com.example.authapp.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthControl {

    private final UserService userService;
    private final AuthService authService;

    public AuthControl(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ApiResponse register(@RequestBody RegistrationRequest request, HttpServletResponse response) {
        User user = userService.registerNewUser(
                request.getName(),
                request.getPassword(),
                request.getEmail(),
                request.getPhoneNo());
        authService.issueToken(user, response);
        return ApiResponse.ok("Registration successful", user.getName());
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.ok("Logged out");
    }
}