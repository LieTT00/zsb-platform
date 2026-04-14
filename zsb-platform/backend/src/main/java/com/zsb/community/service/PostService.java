package com.zsb.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsb.community.dto.PostRequest;
import java.util.Map;

public interface PostService {
    Page<Map<String, Object>> getPostList(int page, int size, String keyword, String school, String sort);
    Map<String, Object> getPostDetail(Long id);
    Long createPost(PostRequest req, Long userId);
    void updatePost(Long id, PostRequest req, Long userId);
    void deletePost(Long id, Long userId, String username);
    boolean toggleLike(Long id, Long userId);
    Page<Map<String, Object>> getMyPosts(Long userId, int page, int size);
}
