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


    /**
     * 댓글을 생성한다.
     * 하나의 일정에는 최대 10개의 댓글만 등록할 수 있다.
     *
     * @param scheduleId 댓글을 등록할 일정 ID
     * @param request 댓글 내용, 작성자, 비밀번호를 포함한 생성 요청
     * @return 생성된 댓글 정보
     * @throws ResponseStatusException 입력값이 유효하지 않으면 400, 일정이 없으면 404를 발생시킨다
     */
    @Transactional
    public CreateCommentResponse save(Long scheduleId, CreateCommentRequest request) {

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 내용은 필수입니다.");
        }
        if (request.getContent().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글은 100자 이하여야 합니다.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        if (request.getWriter() == null || request.getWriter().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자는 필수입니다.");
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));

        long commentCount = commentRepository.countByScheduleId(scheduleId);

        if (commentCount >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글은 최대 10개까지만 작성할 수 있습니다.");
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
