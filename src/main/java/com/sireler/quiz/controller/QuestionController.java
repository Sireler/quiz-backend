package com.sireler.quiz.controller;

import com.sireler.quiz.dto.QuestionRequestDto;
import com.sireler.quiz.dto.QuestionResponseDto;
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
    public List<QuestionResponseDto> listQuestions(@PathVariable("topicId") Long topicId) {
        List<Question> questions = questionService.getQuestionsByTopicId(topicId);
        List<QuestionResponseDto> response = questions
                .stream()
                .map(question -> modelMapper.map(question, QuestionResponseDto.class))
                .collect(Collectors.toList());

        return response;
    }

    @GetMapping("{id}")
    public QuestionResponseDto getQuestion(@PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        QuestionResponseDto response = modelMapper.map(question, QuestionResponseDto.class);

        return response;
    }

    @PostMapping
    public QuestionResponseDto createQuestion(@Valid @RequestBody QuestionRequestDto questionRequestDto,
                                              @PathVariable("topicId") Long topicId) {
        Topic topic = topicService.getTopic(topicId);
        Question question = modelMapper.map(questionRequestDto, Question.class);
        question.setTopic(topic);

        Question createdQuestion = questionService.createQuestion(question);
        QuestionResponseDto response = modelMapper.map(createdQuestion, QuestionResponseDto.class);

        return response;
    }

    @PutMapping("{id}")
    public QuestionResponseDto updateQuestion(@Valid @RequestBody QuestionRequestDto questionRequestDto,
                                              @PathVariable("id") Long id) {
        Question questionToUpdate = modelMapper.map(questionRequestDto, Question.class);

        Question updatedQuestion = questionService.updateQuestion(questionToUpdate, id);
        QuestionResponseDto response = modelMapper.map(updatedQuestion, QuestionResponseDto.class);

        return response;
    }

    @DeleteMapping("{id}")
    public void deleteQuestion(@PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
    }
}
