package com.documentqna.controller;

import com.documentqna.dto.*;
import com.documentqna.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Given
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        UserResponseDto userResponse = new UserResponseDto();
        when(userService.registerUser(registrationDto)).thenReturn(userResponse);

        // When
        ResponseEntity<ApiResponse<UserResponseDto>> response = authController.register(registrationDto);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody().getMessage());
        assertEquals(userResponse, response.getBody().getData());
        verify(userService, times(1)).registerUser(registrationDto);
    }

    @Test
    void testLoginUser() {
        // Given
        UserLoginDto loginDto = new UserLoginDto();
        String token = "mock-jwt-token";
        when(userService.authenticateUser(loginDto)).thenReturn(token);

        // When
        ResponseEntity<ApiResponse<String>> response = authController.login(loginDto);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login successful", response.getBody().getMessage());
        assertEquals(token, response.getBody().getData());
        verify(userService, times(1)).authenticateUser(loginDto);
    }

    @Test
    void testLogoutUser() {
        // When
        ResponseEntity<ApiResponse<Void>> response = authController.logout();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logout successful", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }
}
