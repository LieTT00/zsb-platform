package com.zsb.community.controller;

import com.zsb.community.dto.Result;
import com.zsb.community.entity.Schedule;
import com.zsb.community.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 获取某月的课程表
     */
    @GetMapping("/month")
    public Result<List<Schedule>> getMonth(
            @RequestAttribute("userId") Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate month) {
        return Result.success(scheduleService.getSchedulesByMonth(userId, month));
    }

    /**
     * 创建课程项
     */
    @PostMapping
    public Result<Schedule> create(
            @RequestAttribute("userId") Long userId,
            @RequestBody Schedule schedule) {
        schedule.setUserId(userId);
        return Result.success(scheduleService.create(schedule));
    }

    /**
     * 更新课程项
     */
    @PutMapping("/{id}")
    public Result<Schedule> update(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long id,
            @RequestBody Schedule schedule) {
        schedule.setId(id);
        return Result.success(scheduleService.update(schedule));
    }

    /**
     * 删除课程项
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long id) {
        scheduleService.delete(id, userId);
        return Result.success(null);
    }

    /**
     * 打卡签到
     */
    @PostMapping("/{id}/checkin")
    public Result<Schedule> checkIn(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long id) {
        Schedule schedule = scheduleService.checkIn(id, userId);
        if (schedule == null) {
            return Result.fail("课程不存在或无权操作");
        }
        return Result.success(schedule);
    }

    /**
     * 标记缺席
     */
    @PostMapping("/{id}/absent")
    public Result<Schedule> markAbsent(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long id) {
        Schedule schedule = scheduleService.markAbsent(id, userId);
        if (schedule == null) {
            return Result.fail("课程不存在或无权操作");
        }
        return Result.success(schedule);
    }
}
