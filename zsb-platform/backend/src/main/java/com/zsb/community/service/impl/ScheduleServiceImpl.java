package com.zsb.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.entity.Schedule;
import com.zsb.community.mapper.ScheduleMapper;
import com.zsb.community.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> getSchedulesByMonth(Long userId, LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        return scheduleMapper.selectByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public Schedule create(Schedule schedule) {
        schedule.setAttendanceStatus("none");
        scheduleMapper.insert(schedule);
        return schedule;
    }

    @Override
    public Schedule update(Schedule schedule) {
        scheduleMapper.updateById(schedule);
        return scheduleMapper.selectById(schedule.getId());
    }

    @Override
    public void delete(Long id, Long userId) {
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule != null && schedule.getUserId().equals(userId)) {
            scheduleMapper.deleteById(id);
        }
    }

    @Override
    public Schedule checkIn(Long id, Long userId) {
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule != null && schedule.getUserId().equals(userId)) {
            schedule.setAttendanceStatus("present");
            schedule.setAttendanceTime(LocalDateTime.now());
            scheduleMapper.updateById(schedule);
            return schedule;
        }
        return null;
    }

    @Override
    public Schedule markAbsent(Long id, Long userId) {
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule != null && schedule.getUserId().equals(userId)) {
            schedule.setAttendanceStatus("absent");
            schedule.setAttendanceTime(LocalDateTime.now());
            scheduleMapper.updateById(schedule);
            return schedule;
        }
        return null;
    }
}
