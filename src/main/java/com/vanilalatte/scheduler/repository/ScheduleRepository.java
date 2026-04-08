package com.vanilalatte.scheduler.repository;

import com.vanilalatte.scheduler.entity.Schedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByWriter(String writer, Sort sort);

}
