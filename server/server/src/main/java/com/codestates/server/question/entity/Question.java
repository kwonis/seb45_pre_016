package com.codestates.server.question.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question_title", nullable = false)
    private String title;

    @Column(name = "question_content", nullable = false)
    private String content;

    @Column(name = "question_views", nullable = true)
    private Long views;

    @CreatedDate
    private LocalDateTime created_At;

    @LastModifiedDate
    private LocalDateTime modified_At;

    //매핑 해야함.
    @Column
    private Long userId;
}