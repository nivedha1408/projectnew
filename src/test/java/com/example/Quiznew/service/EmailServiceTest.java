package com.example.Quiznew.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendSimpleEmail("to@example.com", "Subject", "Body");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendRegistrationEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        emailService.sendRegistrationConfirmation("user@example.com", "John");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

