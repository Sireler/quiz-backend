package com.sireler.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sireler.quiz.dto.TopicRequestDto;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.model.User;
import com.sireler.quiz.repository.TopicRepository;
import com.sireler.quiz.repository.UserRepository;
import com.sireler.quiz.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TopicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired UserService userService) {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("testEmail@test");
        user.setPassword("secret");
        userService.register(user);
    }

    @Test
    @WithUserDetails("testUsername")
    void shouldReturnTopic() throws Exception {
        User user = userRepository.findByUsername("testUsername");
        String topicName = "Topic 1 test";
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setUser(user);
        topicRepository.save(topic);

        mockMvc.perform(get("/api/v1/topics/" + topic.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(topicName)));
    }

    @Test
    @WithUserDetails("testUsername")
    void whenTopicNotExists_thenShouldReturnBadRequest() throws Exception {
        User user = userRepository.findByUsername("testUsername");
        String topicName = "Topic 1 test";
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setUser(user);
        topicRepository.save(topic);

        mockMvc.perform(get("/api/v1/topics/999"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Topic not found with id: 999")));
    }

    @Test
    @WithUserDetails("testUsername")
    void whenGivenValidTopic_thenShouldSaveTopic() throws Exception {
        String topicName = "Test create topic";
        TopicRequestDto topicRequestDto = new TopicRequestDto();
        topicRequestDto.setName(topicName);

        mockMvc
                .perform(
                        post("/api/v1/topics")
                                .content(objectMapper.writeValueAsString(topicRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(topicName)));
    }

    @Test
    @WithUserDetails("testUsername")
    void whenGivenValidTopicAndId_thenShouldUpdateTopic() throws Exception {
        User user = userRepository.findByUsername("testUsername");
        Topic topic = new Topic();
        topic.setName("New topic");
        topic.setUser(user);
        topicRepository.save(topic);

        TopicRequestDto topicRequestDto = new TopicRequestDto();
        topicRequestDto.setName("Updated topic name");
        mockMvc
                .perform(
                        put("/api/v1/topics/" + topic.getId())
                                .content(objectMapper.writeValueAsString(topicRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(topicRequestDto.getName())));
    }

    @Test
    @WithUserDetails("testUsername")
    void whenGivenValidId_thenShouldDeleteTopic() throws Exception {
        User user = userRepository.findByUsername("testUsername");
        Topic topic = new Topic();
        topic.setName("New topic");
        topic.setUser(user);
        topicRepository.save(topic);

        mockMvc
                .perform(
                        delete("/api/v1/topics/" + topic.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
