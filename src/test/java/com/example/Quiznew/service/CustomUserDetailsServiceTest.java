package com.example.Quiznew.service;
import com.example.Quiznew.model.Role;
import com.example.Quiznew.model.User;
import com.example.Quiznew.repository.RoleRepository;
import com.example.Quiznew.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(new Role(1L, "ROLE_PARTICIPANT")));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknown");
    }
}

