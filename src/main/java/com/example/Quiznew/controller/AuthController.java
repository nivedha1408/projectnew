package com.example.Quiznew.controller;

import com.example.Quiznew.Dto.UserRegistrationDto;
import com.example.Quiznew.model.User;
import com.example.Quiznew.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Show registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register"; // register.html Thymeleaf template
    }

    /**
     * Handle registration
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto userDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            User user = userService.registerUser(userDto);
        } catch (RuntimeException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "register";
        }

        model.addAttribute("successMessage", "Registration successful! You can now log in.");
        return "login"; // redirect to login page
    }

    /**
     * Show login form
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // If already logged in, redirect based on role
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/quizzes";
            } else {
                return "redirect:/quiz/list";
            }
        }

        return "login"; // login.html Thymeleaf template
    }

    /**
     * Logout handled automatically by Spring Security
     */
}

