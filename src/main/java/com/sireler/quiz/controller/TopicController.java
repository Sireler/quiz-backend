package com.sireler.quiz.controller;

import com.sireler.quiz.dto.TopicDto;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.model.User;
import com.sireler.quiz.security.JwtUserDetails;
import com.sireler.quiz.service.TopicService;
import com.sireler.quiz.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    public TopicController(TopicService topicService,
                           UserService userService,
                           ModelMapper modelMapper) {
        this.topicService = topicService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    public TopicDto getTopic(@PathVariable("id") Long id) {
        Topic topic = topicService.getTopic(id);
        TopicDto topicDto = modelMapper.map(topic, TopicDto.class);

        return topicDto;
    }

    @PostMapping
    public TopicDto createTopic(@Valid @RequestBody TopicDto topicDto,
                                @AuthenticationPrincipal JwtUserDetails jwtUser) {
        User user = userService.findByUsername(jwtUser.getUsername());
        Topic topic = modelMapper.map(topicDto, Topic.class);
        topic.setUser(user);

        Topic createdTopic = topicService.createTopic(topic);
        TopicDto createdTopicDto = modelMapper.map(createdTopic, TopicDto.class);

        return createdTopicDto;
    }

    @PutMapping("{id}")
    public TopicDto updateTopic(@Valid @RequestBody TopicDto topicDto,
                                @PathVariable("id") Long id) {
        Topic topicToUpdate = modelMapper.map(topicDto, Topic.class);

        Topic updatedTopic = topicService.updateTopic(topicToUpdate, id);
        TopicDto updatedTopicDto = modelMapper.map(updatedTopic, TopicDto.class);

        return updatedTopicDto;
    }

    @DeleteMapping("{id}")
    public void deleteTopic(@PathVariable("id") Long id) {
        topicService.deleteTopic(id);
    }
}
