package com.zsb.community.controller;

import com.zsb.community.dto.Result;
import com.zsb.community.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 统计数据接口
 * 提供用户数、帖子数、活跃率等可视化数据
 */
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * 总览数据（注册用户、月活用户、活跃率）
     * GET /api/stats/overview
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        return Result.success(statsService.getOverview());
    }

    /**
     * 近12个月用户增长数据
     * GET /api/stats/monthly
     */
    @GetMapping("/monthly")
    public Result<Map<String, Object>> getMonthly() {
        return Result.success(statsService.getMonthlyStats());
    }

    /**
     * 帖子发布趋势（近30天）
     * GET /api/stats/post-trend
     */
    @GetMapping("/post-trend")
    public Result<Map<String, Object>> getPostTrend() {
        return Result.success(statsService.getPostTrend());
    }

    /**
     * 个人统计数据（需要登录）
     * GET /api/stats/me
     */
    @GetMapping("/me")
    public Result<Map<String, Object>> getPersonalStats(@RequestAttribute("userId") Long userId) {
        return Result.success(statsService.getPersonalStats(userId));
    }
}
