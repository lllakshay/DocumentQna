package com.documentqna.util;


import com.documentqna.entity.Role;
import com.documentqna.entity.User;
import com.documentqna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {
 
 private UserRepository userRepository;
 private PasswordEncoder passwordEncoder;
 
 @Override
 public void run(String... args) {
     createDefaultAdmin();
 }
 
 private void createDefaultAdmin() {
     if (!userRepository.existsByEmail("admin@docmanagement.com")) {
         User admin = User.builder()
                 .email("admin@docmanagement.com")
                 .username("admin")
                 .password(passwordEncoder.encode("admin123"))
                 .role(Role.ADMIN)
                 .build();
         
         userRepository.save(admin);
         log.info("Default admin user created: admin@docmanagement.com / admin123");
     }
 }
}