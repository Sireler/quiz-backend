package com.sireler.quiz.dto;

import com.sireler.quiz.model.Question;
import lombok.Data;

import java.util.List;

@Data
public class TopicDto {

    private Long id;

    private String name;

    private List<QuestionDto> questions;
}
