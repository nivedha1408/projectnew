package com.example.Quiznew.service;

import com.example.Quiznew.Dto.UserRegistrationDto;
import com.example.Quiznew.model.Role;
import com.example.Quiznew.model.User;
import com.example.Quiznew.repository.RoleRepository;
import com.example.Quiznew.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (dto.getRole() == null || dto.getRole().isBlank()) {
            throw new IllegalArgumentException("Role is required");

        }

        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        String normalizedRoleInput = dto.getRole().trim().toUpperCase();
        String roleName = "ROLE_" + normalizedRoleInput;
        System.out.println("Searching for role: " + roleName);



        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findByNameIgnoreCase(roleName)
                .orElseGet(() -> {
                    // As fallback, try without prefix, in case DB stored without "ROLE_"
                    String altRole = normalizedRoleInput;
                    System.out.println("Fallback search without prefix: '" + altRole + "'");
                    return roleRepository.findByNameIgnoreCase(altRole)
                            .orElseThrow(() ->
                                    new RuntimeException("Role not found: " + roleName + " and also not found alt: " + altRole));
                });

        user.setRoles(Collections.singleton(role));
        userRepository.save(user);

        // send email
        emailService.sendSimpleEmail(user.getEmail(), "Registration Successful", "Welcome " + user.getUsername());
        return user;
    }
}
