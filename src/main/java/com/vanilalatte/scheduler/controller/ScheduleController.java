package com.vanilalatte.scheduler.controller;

import com.vanilalatte.scheduler.dto.CreateScheduleRequest;
import com.vanilalatte.scheduler.dto.CreateScheduleResponse;
import com.vanilalatte.scheduler.dto.GetScheduleResponse;
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

    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(@RequestParam(required = false) String writer){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll(writer));
    }

}
