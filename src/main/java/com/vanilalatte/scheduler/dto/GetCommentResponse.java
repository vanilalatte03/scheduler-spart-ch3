package com.vanilalatte.scheduler.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCommentResponse {

    private final Long id;
    private final Long scheduleId;
    private final String content;
    private final String writer;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetCommentResponse(Long id, Long scheduleId, String content, String writer, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
