package com.example.authapp.user;

import com.example.authapp.auth.AuthService;
import com.example.authapp.dto.ApiResponse;
import com.example.authapp.dto.LoginRequest;
import com.example.authapp.exception.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserControl {

    private final UserService userService;
    private final AuthService authService;

    public UserControl(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        User user = userService.authenticate(request.getName(), request.getPassword());
        authService.issueToken(user, response);
        return ApiResponse.ok("Login successful", user.getName());
    }

    @GetMapping("/user/current")
    public ApiResponse currentUser(HttpServletRequest request) {
        String token = authService.tokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException("Not logged in");
        }
        Long uid = authService.resolveUid(token);
        return ApiResponse.ok("Ok", userService.getById(uid).getName());
    }
}