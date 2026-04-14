package com.zsb.community.service;

import java.util.Map;

public interface StatsService {
    Map<String, Object> getOverview();
    Map<String, Object> getMonthlyStats();
    Map<String, Object> getPostTrend();
    
    /**
     * 获取个人统计数据
     * @param userId 用户ID
     * @return 个人统计（帖子数、获赞数、浏览数等）
     */
    Map<String, Object> getPersonalStats(Long userId);
}
