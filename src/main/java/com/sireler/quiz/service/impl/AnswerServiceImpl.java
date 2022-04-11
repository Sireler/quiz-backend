package com.sireler.quiz.service.impl;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.Answer;
import com.sireler.quiz.repository.AnswerRepository;
import com.sireler.quiz.service.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        log.info("Getting answers by question id: {}", questionId);
        return answerRepository.findByQuestion_Id(questionId);
    }

    @Override
    public Answer createAnswer(Answer answer) {
        log.info("Creating answer");
        return answerRepository.save(answer);
    }

    @Override
    public Answer updateAnswer(Answer answerToUpdate, Long id) {
        log.info("Updating answer with id: {}", id);
        Answer foundAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Answer not found with id: " + id));

        foundAnswer.setBody(answerToUpdate.getBody());
        foundAnswer.setCorrect(answerToUpdate.isCorrect());

        return answerRepository.save(foundAnswer);
    }

    @Override
    public void deleteAnswer(Long id) {
        log.info("Deleting answer with id: {}", id);
        answerRepository.deleteById(id);
    }
}
