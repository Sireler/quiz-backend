package com.sireler.quiz.controller;

import com.sireler.quiz.dto.AnswerDto;
import com.sireler.quiz.model.Answer;
import com.sireler.quiz.model.Question;
import com.sireler.quiz.service.AnswerService;
import com.sireler.quiz.service.QuestionService;
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
@RequestMapping("/api/v1/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    private final QuestionService questionService;

    private final ModelMapper modelMapper;

    public AnswerController(AnswerService answerService, QuestionService questionService, ModelMapper modelMapper) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<AnswerDto> listAnswers(@PathVariable("questionId") Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        List<AnswerDto> answerDtos = answers
                .stream()
                .map(answer -> modelMapper.map(answer, AnswerDto.class))
                .collect(Collectors.toList());

        return answerDtos;
    }

    @PostMapping
    public AnswerDto createAnswer(@Valid @RequestBody AnswerDto answerDto,
                                  @PathVariable("questionId") Long questionId) {
        Question question = questionService.getQuestion(questionId);
        Answer answer = modelMapper.map(answerDto, Answer.class);
        answer.setQuestion(question);

        Answer createdAnswer = answerService.createAnswer(answer);
        AnswerDto createdAnswerDto = modelMapper.map(createdAnswer, AnswerDto.class);

        return createdAnswerDto;
    }

    @PutMapping("{id}")
    public AnswerDto updateAnswer(@Valid @RequestBody AnswerDto answerDto,
                                  @PathVariable("id") Long id) {
        Answer answerToUpdate = modelMapper.map(answerDto, Answer.class);

        Answer updatedAnswer = answerService.updateAnswer(answerToUpdate, id);
        AnswerDto updatedAnswerDto = modelMapper.map(updatedAnswer, AnswerDto.class);

        return updatedAnswerDto;
    }

    @DeleteMapping("{id}")
    public void deleteAnswer(@PathVariable("id") Long id) {
        answerService.deleteAnswer(id);
    }
}
