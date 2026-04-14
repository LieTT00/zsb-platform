package com.zsb.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.entity.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    List<Schedule> getSchedulesByMonth(Long userId, LocalDate month);
    Schedule create(Schedule schedule);
    Schedule update(Schedule schedule);
    void delete(Long id, Long userId);
    Schedule checkIn(Long id, Long userId);
    Schedule markAbsent(Long id, Long userId);
}
