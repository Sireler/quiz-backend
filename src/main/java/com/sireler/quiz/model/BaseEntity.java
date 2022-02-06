package com.sireler.quiz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreatedDate
    @JsonProperty("created_at")
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
