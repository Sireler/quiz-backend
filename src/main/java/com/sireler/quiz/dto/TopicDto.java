package com.sireler.quiz.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class TopicDto {

    @NotBlank
    @Size(min = 4, max = 255, message = "name must be between 4 and 255 characters")
    private String name;
}
