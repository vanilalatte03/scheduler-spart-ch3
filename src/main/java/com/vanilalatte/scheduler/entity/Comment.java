package com.vanilalatte.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false, length = 30)
    private String writer;

    @Column(nullable = false, length = 20)
    private String password;

    public Comment(Long scheduleId, String content, String writer, String password){
        this.scheduleId = scheduleId;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

}
