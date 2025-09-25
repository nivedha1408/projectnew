package com.example.Quiznew.service;



import com.example.Quiznew.Dto.QuizSubmissionDto;
import com.example.Quiznew.model.*;
import com.example.Quiznew.repository.QuestionRepository;
import com.example.Quiznew.repository.QuizAttemptRepository;
import com.example.Quiznew.repository.QuizRepository;
import com.example.Quiznew.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    @InjectMocks
    private QuizService quizService;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void testCreateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Sample Quiz");

        when(quizRepository.save(quiz)).thenReturn(quiz);

        Quiz saved = quizService.createQuiz(quiz);

        assertEquals("Sample Quiz", saved.getTitle());
        verify(quizRepository, times(1)).save(quiz);
    }

    @Test
    void testAddQuestionToQuiz() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        Question question = new Question();
        question.setText("What is 2+2?");
        question.setOptions(new ArrayList<>());

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(question)).thenAnswer(i -> i.getArguments()[0]);

        Question saved = quizService.addQuestionToQuiz(1L, question);

        assertEquals("What is 2+2?", saved.getText());
        assertEquals(quiz, saved.getQuiz());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void testSubmitQuiz_WithCorrectOptions() {
        // Mock Principal for logged-in user
        Principal principal = () -> "testuser";

        // Sample user
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        // Sample quiz with 2 questions
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        // Question 1 with 2 options
        Question q1 = new Question();
        q1.setId(101L);
        Option q1Opt1 = new Option();
        q1Opt1.setId(1L);
        q1Opt1.setCorrect(true); // correct
        Option q1Opt2 = new Option();
        q1Opt2.setId(2L);
        q1Opt2.setCorrect(false);
        q1.setOptions(Arrays.asList(q1Opt1, q1Opt2));

        // Question 2 with 2 options
        Question q2 = new Question();
        q2.setId(102L);
        Option q2Opt1 = new Option();
        q2Opt1.setId(3L);
        q2Opt1.setCorrect(false);
        Option q2Opt2 = new Option();
        q2Opt2.setId(4L);
        q2Opt2.setCorrect(true); // correct
        q2.setOptions(Arrays.asList(q2Opt1, q2Opt2));

        // Add questions to quiz
        quiz.setQuestions(Arrays.asList(q1, q2));

        // Selected options by user
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setSelectedOptionIds(Arrays.asList(1L, 3L)); // q1 correct, q2 wrong

        // Mock repository calls
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(quizAttemptRepository.save(any(QuizAttempt.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service
        int score = quizService.submitQuiz(1L, submission, principal);

        // Verify results
        assertEquals(1, score); // only 1 correct answer
        verify(quizAttemptRepository, times(1)).save(any(QuizAttempt.class));
    }


    @Test
    void testGetQuestionsForQuiz() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        Question q1 = new Question();
        Question q2 = new Question();
        List<Question> questions = Arrays.asList(q1, q2);

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(questionRepository.findByQuiz(quiz)).thenReturn(questions);

        List<Question> result = quizService.getQuestionsForQuiz(1L);

        assertEquals(2, result.size());
        verify(questionRepository, times(1)).findByQuiz(quiz);
    }
}

