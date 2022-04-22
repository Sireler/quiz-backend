package com.sireler.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sireler.quiz.dto.QuestionRequestDto;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.repository.QuestionRepository;
import com.sireler.quiz.repository.TopicRepository;
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
class QuestionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void whenGivenTopicId_thenShouldReturnTopicQuestions() throws Exception {
        Topic topic = new Topic();
        topic.setName("Topic");
        topicRepository.save(topic);

        Question question1 = new Question();
        question1.setBody("question 1");
        question1.setTopic(topic);
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setBody("question 2");
        question2.setTopic(topic);
        questionRepository.save(question2);

        mockMvc.perform(get("/api/v1/topics/" + topic.getId() + "/questions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].body", containsInAnyOrder("question 1", "question 2")));
    }

    @Test
    @WithMockUser
    void whenGivenValidQuestion_thenShouldSaveQuestion() throws Exception {
        Topic topic = new Topic();
        topic.setName("Topic");
        topicRepository.save(topic);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        questionRequestDto.setBody("question 1");

        mockMvc
                .perform(
                        post("/api/v1/topics/" + topic.getId() + "/questions")
                                .content(objectMapper.writeValueAsString(questionRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", is("question 1")));
    }

    @Test
    @WithMockUser
    void whenGivenValidQuestionAndId_thenShouldUpdateQuestion() throws Exception {
        Topic topic = new Topic();
        topic.setName("Topic");
        topicRepository.save(topic);

        Question question = new Question();
        question.setBody("question 1");
        question.setTopic(topic);
        questionRepository.save(question);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        questionRequestDto.setBody("New question body");

        mockMvc
                .perform(
                        put("/api/v1/topics/" + topic.getId() + "/questions/" + question.getId())
                                .content(objectMapper.writeValueAsString(questionRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", is("New question body")));
    }

    @Test
    @WithMockUser
    void whenGivenQuestionId_thenShouldDeleteQuestion() throws Exception {
        Topic topic = new Topic();
        topic.setName("Topic");
        topicRepository.save(topic);

        Question question = new Question();
        question.setBody("question 1");
        question.setTopic(topic);
        questionRepository.save(question);

        mockMvc
                .perform(
                        delete("/api/v1/topics/" + topic.getId() + "/questions/" + question.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
