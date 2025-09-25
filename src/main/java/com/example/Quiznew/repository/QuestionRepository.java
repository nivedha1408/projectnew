package com.example.Quiznew.repository;

import com.example.Quiznew.model.Question;
import com.example.Quiznew.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuiz(Quiz quiz);
}
