package com.vanilalatte.scheduler.repository;

import com.vanilalatte.scheduler.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
