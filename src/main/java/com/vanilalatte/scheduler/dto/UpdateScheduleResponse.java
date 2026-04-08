package com.vanilalatte.scheduler.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateScheduleResponse {

    private final Long id;
    private final String title;
    private final String writer;
    private final LocalDateTime modifiedAt;

    public UpdateScheduleResponse(Long id, String title, String writer, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.modifiedAt = modifiedAt;
    }
}
