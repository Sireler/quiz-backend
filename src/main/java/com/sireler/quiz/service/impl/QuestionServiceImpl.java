package com.sireler.quiz.service.impl;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.repository.QuestionRepository;
import com.sireler.quiz.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> getQuestionsByTopicId(Long topicId) {
        log.info("Getting questions by topic id: {}", topicId);
        return questionRepository.findByTopic_Id(topicId);
    }

    @Override
    public Question getQuestion(Long id) {
        log.info("Getting question with id: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Question not found with id: " + id));
    }

    @Override
    public Question createQuestion(Question question) {
        log.info("Creating question");
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question questionToUpdate, Long id) {
        log.info("Updating question with id: {}", id);
        Question foundQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Question not found with id: " + id));

        foundQuestion.setBody(questionToUpdate.getBody());

        return questionRepository.save(foundQuestion);
    }

    @Override
    public void deleteQuestion(Long id) {
        log.info("Deleting question with id: {}", id);
        questionRepository.deleteById(id);
    }
}
