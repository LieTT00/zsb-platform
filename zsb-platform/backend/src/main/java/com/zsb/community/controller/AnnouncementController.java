package com.zsb.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.dto.Result;
import com.zsb.community.entity.Announcement;
import com.zsb.community.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public Result<Page<Announcement>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        return Result.success(announcementService.getPage(page, size, type));
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        announcementService.incrementView(id);
        return Result.success(announcementService.getById(id));
    }
}
