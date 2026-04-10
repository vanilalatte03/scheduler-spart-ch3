package com.vanilalatte.scheduler.repository;

import com.vanilalatte.scheduler.entity.Schedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 작성자별 일정 목록을 수정일 정렬 조건과 함께 조회한다.
    List<Schedule> findByWriter(String writer, Sort sort);

}
