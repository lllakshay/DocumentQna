package com.documentqna.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserLoginDto {
 
 @NotBlank(message = "Email is required")
 @Email(message = "Please provide a valid email")
 private String email;
 
 @NotBlank(message = "Password is required")
 private String password;
}
