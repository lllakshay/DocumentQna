package com.documentqna.controller;

import com.documentqna.dto.ApiResponse;
import com.documentqna.dto.UserResponseDto;
import com.documentqna.entity.User;
import com.documentqna.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

class UserManagementControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserManagementController userManagementController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getCurrentUser_shouldReturnUserResponse() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("adminUser");
        // populate other fields as needed

        // Act
        ResponseEntity<ApiResponse<UserResponseDto>> responseEntity = 
                userManagementController.getCurrentUser(mockUser);

        // Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        ApiResponse<UserResponseDto> apiResponse = responseEntity.getBody();
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getMessage()).isEqualTo("User profile retrieved");
        assertThat(apiResponse.getData()).isNotNull();
        assertThat(apiResponse.getData().getUsername()).isEqualTo("adminUser");
    }
}
