package com.zsb.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.entity.Announcement;
import java.util.Map;

public interface AnnouncementService {
    Page<Announcement> getPage(int page, int size, String type);
    Announcement getById(Long id);
    void incrementView(Long id);
}
