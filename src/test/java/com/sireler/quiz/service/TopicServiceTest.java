package com.sireler.quiz.service;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.model.User;
import com.sireler.quiz.repository.TopicRepository;
import com.sireler.quiz.service.impl.TopicServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    private TopicService topicService;

    @Mock
    private User user;

    @Mock
    private TopicRepository topicRepository;

    @BeforeEach
    void setUp() {
        topicService = new TopicServiceImpl(topicRepository);
    }

    @Test
    void whenIdExists_thenShouldReturnTopic() {
        Long topicId = 1L;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setUser(user);
        topic.setName("Topic");

        Mockito.when(topicRepository.findById(topicId))
                .thenReturn(Optional.of(topic));

        Topic result = topicService.getTopic(topicId);

        Assertions.assertEquals(topic.getId(), result.getId());
    }

    @Test
    void whenIdNotExists_thenShouldThrowException() {
        Long notExistsTopicId = 14L;

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            topicService.getTopic(notExistsTopicId);
        });

        String expectedMessage = "Topic not found with id: " + notExistsTopicId;
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void whenGivenTopic_thenShouldSaveTopic() {
        Long topicId = 1L;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setUser(user);
        topic.setName("Topic");

        Mockito.when(topicRepository.save(topic))
                .thenReturn(topic);

        Topic result = topicService.createTopic(topic);

        Assertions.assertEquals(topic.getId(), result.getId());
    }

    @Test
    void whenGivenTopicAndIdExists_thenShouldUpdateTopic() {
        Long topicId = 1L;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setUser(user);
        topic.setName("Topic");

        Mockito.when(topicRepository.findById(topicId))
                .thenReturn(Optional.of(topic));

        String topicNameToUpdate = "New topic name";
        topic.setName(topicNameToUpdate);

        Mockito.when(topicRepository.save(topic))
                .thenReturn(topic);

        Topic updatedTopic = topicService.updateTopic(topic, topicId);

        Assertions.assertEquals(topicNameToUpdate, updatedTopic.getName());
        Assertions.assertEquals(topic.getId(), updatedTopic.getId());
    }

    @Test
    void whenGivenTopicAndIdNotExists_thenShouldThrowException() {
        Long notExistsTopicId = 14L;
        Long topicId = 1L;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setUser(user);
        topic.setName("Topic");

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            topicService.updateTopic(topic, notExistsTopicId);
        });

        String expectedMessage = "Topic not found with id: " + notExistsTopicId;
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void whenIdExists_thenShouldDeleteTopic() {
        Long topicId = 1L;

        topicService.deleteTopic(topicId);
        topicService.deleteTopic(topicId);

        Mockito.verify(topicRepository, Mockito.times(2))
                .deleteById(topicId);
    }
}
