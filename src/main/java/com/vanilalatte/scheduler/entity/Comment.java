package com.vanilalatte.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일정에 등록되는 댓글 도메인을 표현하는 엔티티다.
 */
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

    /**
     * 댓글 엔티티를 생성한다.
     * 현재 구현은 일정과의 연관관계를 객체 참조 대신 scheduleId 값으로 보관한다.
     *
     * @param scheduleId 댓글이 속한 일정 ID
     * @param content 댓글 내용
     * @param writer 작성자명
     * @param password 댓글 생성 시 입력한 비밀번호
     */
    public Comment(Long scheduleId, String content, String writer, String password){
        this.scheduleId = scheduleId;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

}
