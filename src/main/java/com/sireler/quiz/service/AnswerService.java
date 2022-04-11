package com.sireler.quiz.service;

import com.sireler.quiz.model.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> getAnswersByQuestionId(Long questionId);

    Answer createAnswer(Answer answer);

    Answer updateAnswer(Answer answer, Long id);

    void deleteAnswer(Long id);
}
