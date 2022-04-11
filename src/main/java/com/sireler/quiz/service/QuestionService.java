package com.sireler.quiz.service;

import com.sireler.quiz.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getQuestionsByTopicId(Long topicId);

    Question getQuestion(Long id);

    Question createQuestion(Question question);

    Question updateQuestion(Question question, Long id);

    void deleteQuestion(Long id);
}
