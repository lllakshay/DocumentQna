package com.documentqna.dto;




import com.documentqna.entity.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserRegistrationDto {
 
 @NotBlank(message = "Email is required")
 @Email(message = "Please provide a valid email")
 private String email;
 
 @NotBlank(message = "Username is required")
 @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
 private String username;
 
 @NotBlank(message = "Password is required")
 @Size(min = 6, message = "Password must be at least 6 characters")
 private String password;
 
 private Role role;
}
