package com.sireler.quiz.service;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.Answer;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.repository.AnswerRepository;
import com.sireler.quiz.service.impl.AnswerServiceImpl;
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
class AnswerServiceTest {

    private AnswerService answerService;

    @Mock
    private Question question;

    @Mock
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        answerService = new AnswerServiceImpl(answerRepository);
    }

    @Test
    void whenGivenQuestionId_thenShouldReturnQuestionAnswers() {
        Answer answer1 = new Answer();
        answer1.setId(1L);
        answer1.setQuestion(question);
        answer1.setBody("Answer 1 body");
        answer1.setCorrect(true);

        Answer answer2 = new Answer();
        answer2.setId(2L);
        answer2.setQuestion(question);
        answer2.setBody("Answer 2 body");
        answer2.setCorrect(false);

        Mockito.when(answerRepository.findByQuestion_Id(question.getId()))
                .thenReturn(List.of(answer1, answer2));

        List<Answer> result = answerService.getAnswersByQuestionId(question.getId());
        List<Answer> emptyResult = answerService.getAnswersByQuestionId(14L);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
        Assertions.assertTrue(emptyResult.isEmpty());
    }

    @Test
    void whenGivenAnswer_thenShouldSaveAnswer() {
        Long answerId = 1L;
        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setQuestion(question);
        answer.setBody("Answer body");
        answer.setCorrect(true);

        Mockito.when(answerRepository.save(answer))
                .thenReturn(answer);

        Answer result = answerService.createAnswer(answer);

        Assertions.assertEquals(answer.getId(), result.getId());
    }

    @Test
    void whenGivenAnswerAndIdExists_thenShouldUpdateAnswer() {
        Long answerId = 1L;
        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setQuestion(question);
        answer.setBody("Answer body");
        answer.setCorrect(true);

        Mockito.when(answerRepository.findById(answerId))
                .thenReturn(Optional.of(answer));

        String answerBodyToUpdate = "New answer body";
        boolean answerCorrectToUpdate = false;
        answer.setBody(answerBodyToUpdate);
        answer.setCorrect(answerCorrectToUpdate);

        Mockito.when(answerRepository.save(answer))
                .thenReturn(answer);

        Answer updatedAnswer = answerService.updateAnswer(answer, answerId);

        Assertions.assertEquals(answerBodyToUpdate, updatedAnswer.getBody());
        Assertions.assertEquals(answerCorrectToUpdate, updatedAnswer.isCorrect());
        Assertions.assertEquals(answer.getId(), updatedAnswer.getId());
    }

    @Test
    void whenGivenAnswerAndIdNotExists_thenShouldThrowException() {
        Long notExistsAnswerId = 14L;
        Long answerId = 1L;
        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setQuestion(question);
        answer.setBody("Answer body");
        answer.setCorrect(true);

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            answerService.updateAnswer(answer, notExistsAnswerId);
        });

        String expectedMessage = "Answer not found with id: " + notExistsAnswerId;
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void whenIdExists_thenShouldDeleteAnswer() {
        Long answerId = 1L;

        answerService.deleteAnswer(answerId);
        answerService.deleteAnswer(answerId);

        Mockito.verify(answerRepository, Mockito.times(2))
                .deleteById(answerId);
    }
}
