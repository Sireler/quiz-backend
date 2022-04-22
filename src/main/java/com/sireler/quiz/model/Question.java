package com.sireler.quiz.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question extends BaseEntity {

    @Column(name = "body")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<Answer> answers;
}
