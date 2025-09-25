package com.example.Quiznew.controller;
import com.example.Quiznew.model.Quiz;
import com.example.Quiznew.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;





@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuizService quizService;

    public AdminController(QuizService quizService) {

        this.quizService = quizService;
    }

    @GetMapping("/quizzes")
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "manage_quizzes";
    }

    @GetMapping("/quizzes/create")
    public String createQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        return "create_quiz";
    }

    @PostMapping("/quizzes/create")
    public String createQuiz(@ModelAttribute Quiz quiz) {
        quizService.createQuiz(quiz);
        return "redirect:/admin/quizzes";
    }

    @GetMapping("/quizzes/delete/{id}")
    public String deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/admin/quizzes";

    }


    // Show form to edit quiz
    @GetMapping("/quizzes/edit/{id}")
    public String showEditQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "edit_quiz"; // Thymeleaf template
    }

    // Handle form submission
    @PostMapping("/quizzes/edit/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute("quiz") Quiz quiz) {
        quizService.updateQuiz(id, quiz);
        return "redirect:/admin/quizzes"; // redirect to manage quizzes page
    }




}
