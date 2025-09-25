package com.example.Quiznew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send a simple text email
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@quizapplication.com"); // can override with application.properties
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (MailException e) {
            System.err.println("Error while sending email: " + e.getMessage());
        }
    }

    /**
     * Registration confirmation email
     */
    public void sendRegistrationConfirmation(String to, String username) {
        String subject = "Welcome to QuizApp, " + username + "!";
        String body = "Hello " + username + ",\n\n" +
                "Thank you for registering at QuizApp.\n" +
                "You can now log in and participate in quizzes.\n\n" +
                "Best Regards,\nQuizApp Team";
        sendSimpleEmail(to, subject, body);
    }

    /**
     * Quiz result notification email
     */
    public void sendQuizResult(String to, String quizTitle, int score, int total) {
        String subject = "Your Result for Quiz: " + quizTitle;
        String body = "Hello,\n\n" +
                "You completed the quiz: " + quizTitle + ".\n" +
                "Your Score: " + score + " out of " + total + "\n\n" +
                "Keep practicing to improve!\n\n" +
                "Best Regards,\nQuizApp Team";
        sendSimpleEmail(to, subject, body);
    }

    /**
     * Password reset email
     */
    public void sendPasswordReset(String to, String resetLink) {
        String subject = "Password Reset Request - QuizApp";
        String body = "Hello,\n\n" +
                "We received a request to reset your password.\n" +
                "Click the link below to reset it:\n" +
                resetLink + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best Regards,\nQuizApp Team";
        sendSimpleEmail(to, subject, body);
    }
}