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
