package com.example.Quiznew.repository;

import com.example.Quiznew.model.Question;
import com.example.Quiznew.model.Quiz;
import com.example.Quiznew.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {





    // Default CRUD methods provided by JpaRepository
}


