package com.vanilalatte.scheduler.service;

import com.vanilalatte.scheduler.dto.CreateScheduleRequest;
import com.vanilalatte.scheduler.dto.CreateScheduleResponse;
import com.vanilalatte.scheduler.entity.Schedule;
import com.vanilalatte.scheduler.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {
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
}
