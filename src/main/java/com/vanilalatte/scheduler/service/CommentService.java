package com.vanilalatte.scheduler.service;

import com.vanilalatte.scheduler.dto.CreateCommentRequest;
import com.vanilalatte.scheduler.dto.CreateCommentResponse;
import com.vanilalatte.scheduler.entity.Comment;
import com.vanilalatte.scheduler.entity.Schedule;
import com.vanilalatte.scheduler.repository.CommentRepository;
import com.vanilalatte.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateCommentResponse save(Long scheduleId, CreateCommentRequest request) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));

        long commentCount = commentRepository.countByScheduleId(scheduleId);
        if (commentCount >= 10) {
            throw new IllegalArgumentException("댓글은 최대 10개까지만 작성할 수 있습니다.");
        }

        Comment comment = new Comment(
                schedule.getId(),
                request.getContent(),
                request.getWriter(),
                request.getPassword());

        Comment saveComment = commentRepository.save(comment);

        return new CreateCommentResponse(
                saveComment.getId(),
                saveComment.getScheduleId(),
                saveComment.getContent(),
                saveComment.getWriter(),
                saveComment.getCreatedAt(),
                saveComment.getModifiedAt());
    }
}
