package com.sireler.quiz.repository;

import com.sireler.quiz.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestion_Id(Long questionId);
}
