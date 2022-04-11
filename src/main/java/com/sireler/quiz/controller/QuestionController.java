package com.sireler.quiz.controller;

import com.sireler.quiz.dto.QuestionDto;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.model.Topic;
import com.sireler.quiz.service.QuestionService;
import com.sireler.quiz.service.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/topics/{topicId}/questions")
public class QuestionController {

    private final TopicService topicService;

    private final QuestionService questionService;

    private final ModelMapper modelMapper;

    public QuestionController(TopicService topicService,
                              QuestionService questionService,
                              ModelMapper modelMapper) {
        this.topicService = topicService;
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<QuestionDto> listQuestions(@PathVariable("topicId") Long topicId) {
        List<Question> questions = questionService.getQuestionsByTopicId(topicId);
        List<QuestionDto> questionDtos = questions
                .stream()
                .map(question -> modelMapper.map(question, QuestionDto.class))
                .collect(Collectors.toList());

        return questionDtos;
    }

    @GetMapping("{id}")
    public QuestionDto getQuestion(@PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        QuestionDto questionDto = modelMapper.map(question, QuestionDto.class);

        return questionDto;
    }

    @PostMapping
    public QuestionDto createQuestion(@Valid @RequestBody QuestionDto questionDto,
                                      @PathVariable("topicId") Long topicId) {
        Topic topic = topicService.getTopic(topicId);
        Question question = modelMapper.map(questionDto, Question.class);
        question.setTopic(topic);

        Question createdQuestion = questionService.createQuestion(question);
        QuestionDto createdQuestionDto = modelMapper.map(createdQuestion, QuestionDto.class);

        return createdQuestionDto;
    }

    @PutMapping("{id}")
    public QuestionDto updateQuestion(@Valid @RequestBody QuestionDto questionDto,
                                      @PathVariable("id") Long id) {
        Question questionToUpdate = modelMapper.map(questionDto, Question.class);

        Question updatedQuestion = questionService.updateQuestion(questionToUpdate, id);
        QuestionDto updatedQuestionDto = modelMapper.map(updatedQuestion, QuestionDto.class);

        return updatedQuestionDto;
    }

    @DeleteMapping("{id}")
    public void deleteQuestion(@PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
    }
}
