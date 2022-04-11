package com.sireler.quiz.repository;

import com.sireler.quiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTopic_Id(Long topicId);
}
