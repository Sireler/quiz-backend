package com.sireler.quiz.service;

import com.sireler.quiz.model.Topic;

public interface TopicService {

    Topic getTopic(Long id);

    Topic createTopic(Topic topic);

    Topic updateTopic(Topic topic, Long id);

    void deleteTopic(Long id);
}
