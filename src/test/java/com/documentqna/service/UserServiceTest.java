package com.documentqna.service;

import com.documentqna.dto.UserLoginDto;
import com.documentqna.dto.UserRegistrationDto;
import com.documentqna.dto.UserResponseDto;
import com.documentqna.entity.Role;
import com.documentqna.entity.User;
import com.documentqna.repository.UserRepository;
import com.documentqna.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@example.com");
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setRole(Role.ADMIN);

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1L)
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password("encodedPassword")
                .role(dto.getRole())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto response = userService.registerUser(dto);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(dto.getEmail());
        assertThat(response.getUsername()).isEqualTo(dto.getUsername());
        assertThat(response.getRole()).isEqualTo(dto.getRole());

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_emailAlreadyExists_throwsException() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("exists@example.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void authenticateUser_success() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("login@example.com");
        loginDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token");

        String token = userService.authenticateUser(loginDto);

        assertThat(token).isEqualTo("jwt-token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    void findByEmail_returnsUserOptional() {
        String email = "findme@example.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .username("foundUser")
                .role(Role.VIEWER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);

        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_returnsEmptyOptional() {
        String email = "missing@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertThat(result).isEmpty();

        verify(userRepository).findByEmail(email);
    }
}
