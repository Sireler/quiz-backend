package com.sireler.quiz.service.impl;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.repository.TopicRepository;
import com.sireler.quiz.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic getTopic(Long id) {
        log.info("Getting topic with id: {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Topic not found with id: " + id));
    }

    @Override
    public Topic createTopic(Topic topic) {
        log.info("Creating topic with name: {}", topic.getName());
        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(Topic topicToUpdate, Long id) {
        log.info("Updating topic with id: {}", id);
        Topic foundTopic = topicRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Topic not found with id: " + id));

        foundTopic.setName(topicToUpdate.getName());

        return topicRepository.save(foundTopic);
    }

    @Override
    public void deleteTopic(Long id) {
        log.info("Deleting topic with id: {}", id);
        topicRepository.deleteById(id);
    }
}
