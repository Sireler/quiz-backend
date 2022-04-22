package com.sireler.quiz.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {

    private Long id;

    private String body;

    private List<AnswerDto> answers;
}
