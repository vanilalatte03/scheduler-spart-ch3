package com.vanilalatte.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일정 도메인을 표현하는 엔티티다.
 */
@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false, length = 30)
    private String writer;

    @Column(nullable = false, length = 20)
    private String password;

    /**
     * 일정 엔티티를 생성한다.
     *
     * @param title 일정 제목
     * @param content 일정 내용
     * @param writer 작성자명
     * @param password 수정 및 삭제 시 검증에 사용하는 비밀번호
     */
    public Schedule(String title, String content, String writer, String password) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

    /**
     * 일정의 제목과 작성자만 수정한다.
     * 내용과 비밀번호는 유지된다.
     *
     * @param title 변경할 일정 제목
     * @param writer 변경할 작성자명
     */
    public void updateSchedule(String title, String writer){
        this.title = title;
        this.writer = writer;
    }


}
