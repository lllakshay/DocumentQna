package com.documentqna.controller;


import com.documentqna.dto.UserRegistrationDto;
import com.documentqna.dto.UserLoginDto;
import com.documentqna.dto.UserResponseDto;
import com.documentqna.dto.ApiResponse;
import com.documentqna.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {
    
    private UserService userService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserResponseDto user = userService.registerUser(registrationDto);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", user));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody UserLoginDto loginDto) {
        String token = userService.authenticateUser(loginDto);
        return ResponseEntity.ok(ApiResponse.success("Login successful", token));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // JWT tokens are stateless, so we just return success
        // In production, you might want to implement token blacklisting
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
}
