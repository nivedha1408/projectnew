package com.example.Quiznew.controller;



import com.example.Quiznew.model.Quiz;
import com.example.Quiznew.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private QuizService quizService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuizzes() {
        // Sample quizzes
        Quiz quiz1 = new Quiz();
        quiz1.setId(1L);
        quiz1.setTitle("Java Basics");

        Quiz quiz2 = new Quiz();
        quiz2.setId(2L);
        quiz2.setTitle("Spring Boot");

        List<Quiz> quizzes = Arrays.asList(quiz1, quiz2);

        when(quizService.getAllQuizzes()).thenReturn(quizzes);

        String viewName = adminController.listQuizzes(model);

        // Verify
        verify(model, times(1)).addAttribute("quizzes", quizzes);
        assertEquals("admin/manage_quizzes", viewName);
    }

    @Test
    void testCreateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("New Quiz");

        when(quizService.createQuiz(quiz)).thenReturn(quiz);

        String viewName = adminController.createQuiz(quiz);

        verify(quizService, times(1)).createQuiz(quiz);
        assertEquals("redirect:/admin/quizzes", viewName);
    }

    @Test
    void testUpdateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Updated Quiz");
        quiz.setId(1L);

        when(quizService.updateQuiz(1L, quiz)).thenReturn(quiz);

        String viewName = adminController.updateQuiz(1L, quiz);

        verify(quizService, times(1)).updateQuiz(1L, quiz);
        assertEquals("redirect:/admin/quizzes", viewName);
    }

    @Test
    void testDeleteQuiz() {
        doNothing().when(quizService).deleteQuiz(1L);

        String viewName = adminController.deleteQuiz(1L);

        verify(quizService, times(1)).deleteQuiz(1L);
        assertEquals("redirect:/admin/quizzes", viewName);
    }
}

