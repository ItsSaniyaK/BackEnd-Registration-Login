package com.example.authapp.dto;

public class ApiResponse {

    private boolean success;
    private String message;
    private String username;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, String username) {
        this.success = success;
        this.message = message;
        this.username = username;
    }

    public static ApiResponse ok(String message) {
        return new ApiResponse(true, message, null);
    }

    public static ApiResponse ok(String message, String username) {
        return new ApiResponse(true, message, username);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}