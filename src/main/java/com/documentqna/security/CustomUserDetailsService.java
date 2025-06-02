package com.documentqna.security;

import com.documentqna.entity.User;
import com.documentqna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
 
 private UserRepository userRepository;
 
 @Override
 @Transactional
 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     User user = userRepository.findByEmail(email)
             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
     
     return user;
 }
}