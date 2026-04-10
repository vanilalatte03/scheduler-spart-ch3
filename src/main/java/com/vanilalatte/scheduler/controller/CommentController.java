package com.vanilalatte.scheduler.controller;

import com.vanilalatte.scheduler.dto.CreateCommentRequest;
import com.vanilalatte.scheduler.dto.CreateCommentResponse;
import com.vanilalatte.scheduler.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 일정에 댓글을 등록하는 요청을 처리한다.
     *
     * @param scheduleId 댓글을 등록할 일정 ID
     * @param request 댓글 생성 요청 본문
     * @return 생성된 댓글 정보와 201 Created 응답
     */
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(@PathVariable Long scheduleId, @RequestBody CreateCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(scheduleId, request));
    }


}
