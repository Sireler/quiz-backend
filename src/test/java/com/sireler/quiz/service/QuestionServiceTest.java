package com.sireler.quiz.service;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.repository.QuestionRepository;
import com.sireler.quiz.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    private QuestionService questionService;

    @Mock
    private Topic topic;

    @Mock
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionService = new QuestionServiceImpl(questionRepository);
    }

    @Test
    void whenIdExists_thenShouldReturnQuestion() {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        question.setTopic(topic);
        question.setBody("Question body");

        Mockito.when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(question));

        Question result = questionService.getQuestion(questionId);

        Assertions.assertEquals(question.getId(), result.getId());
    }

    @Test
    void whenIdNotExists_thenShouldThrowException() {
        Long notExistsQuestionId = 14L;

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            questionService.getQuestion(notExistsQuestionId);
        });

        String expectedMessage = "Question not found with id: " + notExistsQuestionId;
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void whenGivenTopicId_thenShouldReturnTopicQuestions() {
        Question question1 = new Question();
        question1.setId(1L);
        question1.setTopic(topic);
        question1.setBody("Question 1 body");

        Question question2 = new Question();
        question2.setId(2L);
        question2.setTopic(topic);
        question2.setBody("Question 2 body");

        Mockito.when(questionRepository.findByTopic_Id(topic.getId()))
                .thenReturn(List.of(question1, question2));

        List<Question> result = questionService.getQuestionsByTopicId(topic.getId());
        List<Question> emptyResult = questionService.getQuestionsByTopicId(14L);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
        Assertions.assertTrue(emptyResult.isEmpty());
    }

    @Test
    void whenGivenQuestion_thenShouldSaveQuestion() {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        question.setTopic(topic);
        question.setBody("Question body");

        Mockito.when(questionRepository.save(question))
                .thenReturn(question);

        Question result = questionService.createQuestion(question);

        Assertions.assertEquals(question.getId(), result.getId());
    }

    @Test
    void whenGivenQuestionAndIdExists_thenShouldUpdateQuestion() {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        question.setTopic(topic);
        question.setBody("Question body");

        Mockito.when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(question));

        String questionBodyToUpdate = "New question body";
        question.setBody(questionBodyToUpdate);

        Mockito.when(questionRepository.save(question))
                .thenReturn(question);

        Question updatedQuestion = questionService.updateQuestion(question, questionId);

        Assertions.assertEquals(questionBodyToUpdate, updatedQuestion.getBody());
        Assertions.assertEquals(question.getId(), updatedQuestion.getId());
    }

    @Test
    void whenGivenQuestionAndIdNotExists_thenShouldThrowException() {
        Long notExistsQuestionId = 14L;
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        question.setTopic(topic);
        question.setBody("Question body");

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            questionService.updateQuestion(question, notExistsQuestionId);
        });

        String expectedMessage = "Question not found with id: " + notExistsQuestionId;
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void whenIdExists_thenShouldDeleteQuestion() {
        Long questionId = 1L;

        questionService.deleteQuestion(questionId);
        questionService.deleteQuestion(questionId);

        Mockito.verify(questionRepository, Mockito.times(2))
                .deleteById(questionId);
    }
}
