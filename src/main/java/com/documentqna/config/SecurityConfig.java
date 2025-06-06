package com.documentqna.config;


import com.documentqna.security.JwtAuthenticationEntryPoint;
import com.documentqna.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
 
 private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
 private final JwtAuthenticationFilter jwtAuthenticationFilter ;
 
 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }
 
 @Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
     return config.getAuthenticationManager();
 }
 
 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http.cors().and().csrf().disable()
             .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
             .and()
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
             .and()
             .authorizeRequests()
                 .antMatchers("/api/auth/**").permitAll()
                 .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                 .antMatchers("/actuator/**").permitAll()
                 .anyRequest().authenticated();
     
     http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
     
     return http.build();
 }
}
