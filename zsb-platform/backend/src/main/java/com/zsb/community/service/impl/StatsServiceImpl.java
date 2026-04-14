package com.zsb.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zsb.community.entity.Comment;
import com.zsb.community.entity.Post;
import com.zsb.community.entity.User;
import com.zsb.community.mapper.CommentMapper;
import com.zsb.community.mapper.PostMapper;
import com.zsb.community.mapper.UserMapper;
import com.zsb.community.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 统计服务实现
 */
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Override
    public Map<String, Object> getOverview() {
        long totalUsers = userMapper.selectCount(new LambdaQueryWrapper<>());
        long totalPosts = postMapper.selectCount(new LambdaQueryWrapper<>());
        long monthlyActive = userMapper.countMonthlyActive();
        double activeRate = totalUsers > 0 ? Math.round((double) monthlyActive / totalUsers * 1000) / 10.0 : 0.0;

        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", totalUsers);
        data.put("totalPosts", totalPosts);
        data.put("monthlyActive", monthlyActive);
        data.put("activeRate", activeRate);
        return data;
    }

    @Override
    public Map<String, Object> getMonthlyStats() {
        List<String> months = new ArrayList<>();
        List<Long> newUsers = new ArrayList<>();
        List<Long> activeUsers = new ArrayList<>();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate now = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String yearMonth = month.format(fmt);
            months.add(month.getMonthValue() + "月");
            newUsers.add(userMapper.countByYearMonth(yearMonth));
            // 活跃用户近似为新用户的 0.7~0.9 随机（演示用，实际应按登录记录统计）
            activeUsers.add(Math.round(newUsers.get(newUsers.size() - 1) * (0.7 + Math.random() * 0.2)));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("months", months);
        data.put("newUsers", newUsers);
        data.put("activeUsers", activeUsers);
        return data;
    }

    @Override
    public Map<String, Object> getPostTrend() {
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        // 近30天帖子数（简化实现，实际应按天分组统计）
        for (int i = 29; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
            // 实际：按 DATE(create_time) = date 分组统计
            counts.add((long) (Math.random() * 20 + 5));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("dates", dates);
        data.put("counts", counts);
        data.put("schoolRank", postMapper.countBySchool());
        data.put("categoryStats", postMapper.countByCategory());
        return data;
    }

    @Override
    public Map<String, Object> getPersonalStats(Long userId) {
        Map<String, Object> data = new HashMap<>();
        
        // 我的帖子数
        long myPostCount = postMapper.selectCount(
            new LambdaQueryWrapper<Post>().eq(Post::getAuthorId, userId)
        );
        
        // 我的总浏览量
        List<Post> myPosts = postMapper.selectList(
            new LambdaQueryWrapper<Post>().eq(Post::getAuthorId, userId)
        );
        long totalViews = myPosts.stream().mapToLong(p -> p.getViewCount() != null ? p.getViewCount() : 0).sum();
        
        // 我的总获赞数
        long totalLikes = myPosts.stream().mapToLong(p -> p.getLikeCount() != null ? p.getLikeCount() : 0).sum();
        
        // 我的评论数
        long myCommentCount = commentMapper.selectCount(
            new LambdaQueryWrapper<Comment>().eq(Comment::getAuthorId, userId)
        );
        
        data.put("postCount", myPostCount);
        data.put("totalViews", totalViews);
        data.put("totalLikes", totalLikes);
        data.put("commentCount", myCommentCount);
        
        return data;
    }
}
