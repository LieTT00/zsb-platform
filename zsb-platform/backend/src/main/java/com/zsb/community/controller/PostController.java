package com.zsb.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.dto.PostRequest;
import com.zsb.community.dto.Result;
import com.zsb.community.entity.Post;
import com.zsb.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * 帖子接口
 * 提供帖子 CRUD、分页查询、点赞等功能
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 分页查询帖子列表
     * GET /api/post/list?page=1&size=10&keyword=xxx&school=武汉大学&sort=latest
     */
    @GetMapping("/list")
    public Result<Page<Map<String, Object>>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String school,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return Result.success(postService.getPostList(page, size, keyword, school, sort));
    }

    /**
     * 获取帖子详情
     * GET /api/post/{id}
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getDetail(@PathVariable Long id) {
        return Result.success(postService.getPostDetail(id));
    }

    /**
     * 发布帖子
     * POST /api/post
     */
    @PostMapping
    public Result<Long> createPost(
            @Valid @RequestBody PostRequest req,
            @RequestAttribute("userId") Long userId
    ) {
        Long postId = postService.createPost(req, userId);
        return Result.success(postId);
    }

    /**
     * 修改帖子
     * PUT /api/post/{id}
     */
    @PutMapping("/{id}")
    public Result<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest req,
            @RequestAttribute("userId") Long userId
    ) {
        postService.updatePost(id, req, userId);
        return Result.success("修改成功");
    }

    /**
     * 删除帖子
     * DELETE /api/post/{id}
     * 管理员可以删除任意帖子，普通用户只能删除自己的帖子
     */
    @DeleteMapping("/{id}")
    public Result<?> deletePost(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId,
            @RequestAttribute(value = "username", required = false) String username
    ) {
        postService.deletePost(id, userId, username);
        return Result.success("删除成功");
    }

    /**
     * 点赞/取消点赞
     * POST /api/post/{id}/like
     */
    @PostMapping("/{id}/like")
    public Result<Boolean> toggleLike(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId
    ) {
        boolean liked = postService.toggleLike(id, userId);
        return Result.success(liked);
    }

    /**
     * 查询我的帖子
     * GET /api/post/my
     */
    @GetMapping("/my")
    public Result<Page<Map<String, Object>>> getMyPosts(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return Result.success(postService.getMyPosts(userId, page, size));
    }
}
