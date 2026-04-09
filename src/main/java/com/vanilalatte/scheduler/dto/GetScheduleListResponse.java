package com.vanilalatte.scheduler.dto;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 일정 전체 조회용 DTO
 */
@Getter
public class GetScheduleListResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String writer;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetScheduleListResponse(Long id, String title, String content, String writer, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
