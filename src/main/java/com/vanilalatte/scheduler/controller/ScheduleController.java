package com.vanilalatte.scheduler.controller;

import com.vanilalatte.scheduler.dto.*;
import com.vanilalatte.scheduler.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 일정 생성 요청을 처리한다.
     *
     * @param request 일정 생성 요청 본문
     * @return 생성된 일정 정보와 201 Created 응답
     */
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    /**
     * 단건 일정 조회 요청을 처리한다.
     *
     * @param scheduleId 조회할 일정 ID
     * @return 일정 상세 정보와 200 OK 응답
     */
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    /**
     * 일정 목록 조회 요청을 처리한다.
     * writer 파라미터가 있으면 해당 작성자의 일정만 조회한다.
     *
     * @param writer 작성자 필터, 없으면 전체 조회
     * @return 일정 목록과 200 OK 응답
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleListResponse>> getSchedules(@RequestParam(required = false) String writer) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll(writer));
    }

    /**
     * 일정 수정 요청을 처리한다.
     *
     * @param scheduleId 수정할 일정 ID
     * @param request 수정 요청 본문
     * @return 수정된 일정 정보와 200 OK 응답
     */
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedules(
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(scheduleId,request));
    }

    /**
     * 일정 삭제 요청을 처리한다.
     * 요청 본문에는 비밀번호가 포함되어야 한다.
     *
     * @param scheduleId 삭제할 일정 ID
     * @param request 삭제 요청 본문
     * @return 본문 없는 204 No Content 응답
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @RequestBody DeleteScheduleRequest request) {
        scheduleService.delete(scheduleId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
