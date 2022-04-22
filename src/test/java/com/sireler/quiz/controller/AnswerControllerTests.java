package com.sireler.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sireler.quiz.dto.AnswerRequestDto;
import com.sireler.quiz.model.Answer;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.repository.AnswerRepository;
import com.sireler.quiz.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AnswerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void whenGivenQuestionId_thenShouldReturnQuestionAnswers() throws Exception {
        Question question = new Question();
        question.setBody("question 1");
        questionRepository.save(question);

        Answer answer1 = new Answer();
        answer1.setBody("answer 1");
        answer1.setCorrect(true);
        answer1.setQuestion(question);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setBody("answer 2");
        answer2.setCorrect(false);
        answer2.setQuestion(question);
        answerRepository.save(answer2);

        mockMvc.perform(get("/api/v1/questions/" + question.getId() + "/answers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].body", containsInAnyOrder("answer 1", "answer 2")));
    }

    @Test
    @WithMockUser
    void whenGivenValidQuestion_thenShouldSaveQuestion() throws Exception {
        Question question = new Question();
        question.setBody("question 1");
        questionRepository.save(question);

        AnswerRequestDto answerRequestDto = new AnswerRequestDto();
        answerRequestDto.setBody("answer 1");
        answerRequestDto.setCorrect(true);

        mockMvc
                .perform(
                        post("/api/v1/questions/" + question.getId() + "/answers")
                                .content(objectMapper.writeValueAsString(answerRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", is("answer 1")));
    }

    @Test
    @WithMockUser
    void whenGivenValidQuestionAndId_thenShouldUpdateQuestion() throws Exception {
        Question question = new Question();
        question.setBody("question 1");
        questionRepository.save(question);

        Answer answer = new Answer();
        answer.setBody("answer 1");
        answer.setCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        AnswerRequestDto answerRequestDto = new AnswerRequestDto();
        answerRequestDto.setBody("New answer body");
        answerRequestDto.setCorrect(true);

        mockMvc
                .perform(
                        put("/api/v1/questions/" + question.getId() + "/answers/" + answer.getId())
                                .content(objectMapper.writeValueAsString(answerRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", is("New answer body")));
    }

    @Test
    @WithMockUser
    void whenGivenQuestionId_thenShouldDeleteQuestion() throws Exception {
        Question question = new Question();
        question.setBody("question 1");
        questionRepository.save(question);

        Answer answer = new Answer();
        answer.setBody("answer 1");
        answer.setCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        mockMvc
                .perform(
                        delete("/api/v1/questions/" + question.getId() + "/answers/" + answer.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
