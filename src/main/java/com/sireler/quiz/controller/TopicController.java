package com.sireler.quiz.controller;

import com.sireler.quiz.dto.TopicRequestDto;
import com.sireler.quiz.dto.TopicResponseDto;
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
    public TopicResponseDto getTopic(@PathVariable("id") Long id) {
        Topic topic = topicService.getTopic(id);
        TopicResponseDto response = modelMapper.map(topic, TopicResponseDto.class);

        return response;
    }

    @PostMapping
    public TopicResponseDto createTopic(@Valid @RequestBody TopicRequestDto topicRequestDto,
                                        @AuthenticationPrincipal JwtUserDetails jwtUser) {
        User user = userService.findByUsername(jwtUser.getUsername());
        Topic topic = modelMapper.map(topicRequestDto, Topic.class);
        topic.setUser(user);

        Topic createdTopic = topicService.createTopic(topic);
        TopicResponseDto response =
                modelMapper.map(createdTopic, TopicResponseDto.class);

        return response;
    }

    @PutMapping("{id}")
    public TopicResponseDto updateTopic(@Valid @RequestBody TopicRequestDto topicRequestDto,
                                        @PathVariable("id") Long id) {
        Topic topicToUpdate = modelMapper.map(topicRequestDto, Topic.class);

        Topic updatedTopic = topicService.updateTopic(topicToUpdate, id);
        TopicResponseDto response =
                modelMapper.map(updatedTopic, TopicResponseDto.class);

        return response;
    }

    @DeleteMapping("{id}")
    public void deleteTopic(@PathVariable("id") Long id) {
        topicService.deleteTopic(id);
    }
}
