package com.sireler.quiz.dto;

import lombok.Data;

@Data
public class AnswerDto {

    private Long id;

    private String body;

    private boolean isCorrect;
}
