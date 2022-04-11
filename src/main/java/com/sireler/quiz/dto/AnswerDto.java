package com.sireler.quiz.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AnswerDto {

    @NotBlank
    private String body;

    @NotNull
    private boolean isCorrect;
}
