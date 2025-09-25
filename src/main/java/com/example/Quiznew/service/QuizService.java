package com.example.Quiznew.service;

import com.example.Quiznew.Dto.QuizSubmissionDto;
import com.example.Quiznew.model.*;
import com.example.Quiznew.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;




@Service
public class QuizService {
    @Autowired
    private final QuizRepository quizRepository;
    @Autowired
    private final QuestionRepository questionRepository;
    @Autowired
    private final OptionRepository optionRepository;
    @Autowired
    private final QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private final UserRepository userRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository,
                       OptionRepository optionRepository, QuizAttemptRepository quizAttemptRepository,
                       UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.userRepository = userRepository;
    }



    // Fetch all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public List<Question> getQuestionsForQuiz(Long quizId) {
        Quiz quiz = getQuizById(quizId); // fetch quiz
        return questionRepository.findByQuiz(quiz);
    }


    // Get quiz by id
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // Create new quiz with questions and options
    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getQuestions() != null) {
            for (Question q : quiz.getQuestions()) {
                q.setQuiz(quiz);
                if (q.getOptions() != null) {
                    for (Option opt : q.getOptions()) {
                        opt.setQuestion(q);
                    }
                }
            }
        }
        return quizRepository.save(quiz);
    }

    // Submit quiz and calculate score
    @Transactional
    public int submitQuiz(Long quizId, QuizSubmissionDto submission, Principal principal) {
        Quiz quiz = getQuizById(quizId);

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        int totalScore = 0;
        List<Long> selectedOptionIds = submission.getSelectedOptionIds();

        for (Question q : quiz.getQuestions()) {
            for (Option opt : q.getOptions()) {
                if (opt.isCorrect() && selectedOptionIds.contains(opt.getId())) {
                    totalScore++;
                }
            }
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setSelectedOptionIds(selectedOptionIds);
        attempt.setScore(totalScore);

        quizAttemptRepository.save(attempt);
        return totalScore;
    }

    @Transactional
    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Associate question with quiz
        question.setQuiz(quiz);

        // Set quiz for each option
        if (question.getOptions() != null) {
            for (Option opt : question.getOptions()) {
                opt.setQuestion(question);
            }
        }


        // Save question
        Question savedQuestion = questionRepository.save(question);

        // Add to quiz's question list (optional)
        quiz.getQuestions().add(savedQuestion);

        return savedQuestion;
    }




    // Delete quiz
    @Transactional
    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }


    public Quiz updateQuiz(Long quizId, Quiz updatedQuiz) {
        return quizRepository.findById(quizId).map(existing -> {
            existing.setTitle(updatedQuiz.getTitle());
            existing.setDescription(updatedQuiz.getDescription());
            existing.setTimeLimit(updatedQuiz.getTimeLimit());
            // optionally update questions if included
            if (updatedQuiz.getQuestions() != null) {
                existing.setQuestions(updatedQuiz.getQuestions());
            }
            return quizRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Quiz not found with ID " + quizId));
    }



}

