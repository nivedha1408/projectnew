package com.example.Quiznew.controller;

import com.example.Quiznew.Dto.QuizSubmissionDto;
import com.example.Quiznew.model.Question;
import com.example.Quiznew.model.Quiz;
import com.example.Quiznew.model.QuizAttempt;
import com.example.Quiznew.service.QuizService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/list")
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "quiz_list";
    }

    @GetMapping("/take/{id}")
    public String takeQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "take_quiz";
    }

    @PostMapping("/submit/{id}")
    public String submitQuiz(@PathVariable Long id,
                             @ModelAttribute QuizSubmissionDto submission,
                             Principal principal,
                             Model model) {
        int score = quizService.submitQuiz(id, submission, principal);
        model.addAttribute("score", score);
        model.addAttribute("quizTitle", quizService.getQuizById(id).getTitle());
        return "quiz_result";
    }
}