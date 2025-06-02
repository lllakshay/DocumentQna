package com.documentqna.controller;


import com.documentqna.dto.UserResponseDto;
import com.documentqna.dto.ApiResponse;
import com.documentqna.entity.User;
import com.documentqna.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "User Management", description = "Admin endpoints for user management")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {
 
 private UserService userService;
 
 @GetMapping("/me")
 @Operation(summary = "Get current user profile")
 @PreAuthorize("isAuthenticated()")
 public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(@AuthenticationPrincipal User user) {
     UserResponseDto userResponse = UserResponseDto.from(user);
     return ResponseEntity.ok(ApiResponse.success("User profile retrieved", userResponse));
 }
 
 // Additional admin endpoints can be added here
 // For brevity, keeping minimal implementation
}
