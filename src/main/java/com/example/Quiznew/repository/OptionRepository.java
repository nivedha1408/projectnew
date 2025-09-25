package com.example.Quiznew.repository;

import com.example.Quiznew.model.Option;
import com.example.Quiznew.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByQuestion(Question question);

    List<Option> findByQuestionId(Long questionId);
}
