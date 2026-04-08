package com.vanilalatte.scheduler.service;

import com.vanilalatte.scheduler.dto.CreateScheduleRequest;
import com.vanilalatte.scheduler.dto.CreateScheduleResponse;
import com.vanilalatte.scheduler.dto.GetScheduleResponse;
import com.vanilalatte.scheduler.entity.Schedule;
import com.vanilalatte.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    public GetScheduleResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("해당 일정이 없습니다.")
        );
        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getWriter(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> findAll(String writer) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");

        List<Schedule> schedules;
        if (writer == null || writer.isBlank()) {
            schedules = scheduleRepository.findAll(sort);
        } else {
            schedules = scheduleRepository.findByWriter(writer, sort);
        }

        List<GetScheduleResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            dtos.add(new GetScheduleResponse(
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
}
