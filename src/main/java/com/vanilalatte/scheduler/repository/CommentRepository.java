package com.vanilalatte.scheduler.repository;

import com.vanilalatte.scheduler.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 일정별 댓글 개수를 조회해 최대 댓글 수 제한 검증에 사용한다.
    long countByScheduleId(Long scheduleId);

    // 일정 상세 조회 시 함께 내려줄 댓글 목록을 조회한다.
    List<Comment> findAllByScheduleId(Long scheduleId);
}
