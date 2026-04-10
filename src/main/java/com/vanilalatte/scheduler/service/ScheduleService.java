package com.vanilalatte.scheduler.service;

import com.vanilalatte.scheduler.dto.*;
import com.vanilalatte.scheduler.entity.Comment;
import com.vanilalatte.scheduler.entity.Schedule;
import com.vanilalatte.scheduler.repository.CommentRepository;
import com.vanilalatte.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    /**
     * 일정을 생성한다.
     *
     * @param request 일정 제목, 내용, 작성자, 비밀번호를 포함한 생성 요청
     * @return 생성된 일정 정보
     * @throws ResponseStatusException 제목/내용/작성자/비밀번호가 비어 있거나 길이 제한을 초과하면 400 Bad Request를 발생시킨다
     */
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다.");
        }

        if (request.getTitle().length() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 30자 이하여야 합니다.");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 내용은 필수입니다.");
        }

        if (request.getContent().length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 내용은 200자 이하여야 합니다.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        if (request.getWriter() == null || request.getWriter().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자는 필수입니다.");
        }

        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getWriter(),
                request.getPassword()
        );
        Schedule saveSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
                saveSchedule.getId(),
                saveSchedule.getTitle(),
                saveSchedule.getContent(),
                saveSchedule.getWriter(),
                saveSchedule.getCreatedAt(),
                saveSchedule.getModifiedAt()
        );
    }


    /**
     * 일정 상세 정보를 조회한다.
     * 조회 결과에는 해당 일정에 등록된 댓글 목록이 함께 포함된다.
     *
     * @param scheduleId 조회할 일정 ID
     * @return 일정 본문과 댓글 목록을 포함한 응답
     * @throws ResponseStatusException 일정이 존재하지 않으면 404 Not Found를 발생시킨다
     */
    @Transactional(readOnly = true)
    public GetScheduleResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);
        List<GetCommentResponse> getCommentResponses = new ArrayList<>();
        for(Comment comment : comments){
            getCommentResponses.add(new GetCommentResponse(
                    comment.getId(),
                    comment.getScheduleId(),
                    comment.getContent(),
                    comment.getWriter(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()
            ));
        }

        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getWriter(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                getCommentResponses
        );
    }

    /**
     * 일정 목록을 조회한다.
     * 작성자명이 주어지면 해당 작성자의 일정만 수정일 내림차순으로 반환한다.
     *
     * @param writer 조회할 작성자명, 비어 있으면 전체 일정을 조회한다
     * @return 수정일 내림차순으로 정렬된 일정 목록
     */
    @Transactional(readOnly = true)
    public List<GetScheduleListResponse> findAll(String writer) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");

        List<Schedule> schedules;
        if (writer == null || writer.isBlank()) {
            schedules = scheduleRepository.findAll(sort);
        } else {
            schedules = scheduleRepository.findByWriter(writer, sort);
        }

        List<GetScheduleListResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            dtos.add(new GetScheduleListResponse(
                    schedule.getId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getWriter(),
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
            ));
        }
        return dtos;
    }

    /**
     * 일정의 제목과 작성자를 수정한다.
     * 비밀번호가 일치해야 하며 내용과 비밀번호는 변경하지 않는다.
     *
     * @param scheduleId 수정할 일정 ID
     * @param request 변경할 제목, 작성자, 비밀번호를 포함한 수정 요청
     * @return 수정된 일정 정보
     * @throws ResponseStatusException 제목/작성자/비밀번호가 유효하지 않으면 400, 일정이 없으면 404, 비밀번호가 일치하지 않으면 403을 발생시킨다
     */
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다.");
        }

        if (request.getTitle().length() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 30자 이하여야 합니다.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        if (request.getWriter() == null || request.getWriter().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자는 필수입니다.");
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        schedule.updateSchedule(request.getTitle(), request.getWriter());

        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getWriter(),
                schedule.getModifiedAt()
        );
    }

    /**
     * 일정을 삭제한다.
     * 삭제 요청에는 일정 생성 시 사용한 비밀번호가 포함되어야 한다.
     *
     * @param scheduleId 삭제할 일정 ID
     * @param request 비밀번호를 포함한 삭제 요청
     * @throws ResponseStatusException 비밀번호가 비어 있으면 400, 일정이 없으면 404, 비밀번호가 일치하지 않으면 403을 발생시킨다
     */
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.deleteById(scheduleId);
    }
}
