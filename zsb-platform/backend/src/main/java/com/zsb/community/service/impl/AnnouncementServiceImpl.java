package com.zsb.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.entity.Announcement;
import com.zsb.community.mapper.AnnouncementMapper;
import com.zsb.community.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    public Page<Announcement> getPage(int page, int size, String type) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Announcement::getType, type);
        }
        wrapper.orderByDesc(Announcement::getPinned)
               .orderByDesc(Announcement::getCreateTime);
        
        return announcementMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Announcement getById(Long id) {
        return announcementMapper.selectById(id);
    }

    @Override
    public void incrementView(Long id) {
        // 简单实现，实际可用 Redis 缓存
        Announcement ann = announcementMapper.selectById(id);
        if (ann != null) {
            ann.setViewCount(ann.getViewCount() == null ? 1 : ann.getViewCount() + 1);
            announcementMapper.updateById(ann);
        }
    }
}
