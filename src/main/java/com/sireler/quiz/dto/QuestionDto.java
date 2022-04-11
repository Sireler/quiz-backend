package com.sireler.quiz.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QuestionDto {

    @NotBlank
    private String body;
}
