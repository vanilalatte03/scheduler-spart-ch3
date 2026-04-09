package com.vanilalatte.scheduler.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequest {

    private String content;
    private String writer;
    private String password;

}
