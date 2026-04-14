package com.zsb.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsb.community.dto.PostRequest;
import com.zsb.community.entity.Post;
import com.zsb.community.entity.User;
import com.zsb.community.mapper.PostMapper;
import com.zsb.community.mapper.UserMapper;
import com.zsb.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 帖子服务实现
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String LIKE_KEY_PREFIX = "post:like:";
    private static final String VIEW_KEY_PREFIX = "post:view:";

    @Override
    public Page<Map<String, Object>> getPostList(int page, int size, String keyword, String school, String sort) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, 0);

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Post::getTitle, keyword).or().like(Post::getContent, keyword));
        }
        if (StringUtils.hasText(school)) {
            wrapper.eq(Post::getTargetSchool, school);
        }
        // 排序
        switch (sort) {
            case "hot" -> wrapper.orderByDesc(Post::getViewCount);
            case "likes" -> wrapper.orderByDesc(Post::getLikeCount);
            default -> wrapper.orderByDesc(Post::getCreateTime);
        }

        Page<Post> postPage = postMapper.selectPage(new Page<>(page, size), wrapper);
        Page<Map<String, Object>> resultPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        resultPage.setRecords(postPage.getRecords().stream().map(this::buildPostVO).toList());
        return resultPage;
    }

    @Override
    public Map<String, Object> getPostDetail(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) throw new RuntimeException("帖子不存在");

        // 浏览量 +1（Redis防重）
        String viewKey = VIEW_KEY_PREFIX + id;
        redisTemplate.opsForValue().increment(viewKey);
        post.setViewCount(post.getViewCount() + 1);
        Post update = new Post();
        update.setId(id);
        update.setViewCount(post.getViewCount());
        postMapper.updateById(update);

        Map<String, Object> vo = buildPostVO(post);
        // 查询作者
        User author = userMapper.selectById(post.getAuthorId());
        if (author != null) {
            vo.put("authorSchool", author.getTargetSchool());
        }
        return vo;
    }

    @Override
    public Long createPost(PostRequest req, Long userId) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setAuthorId(userId);
        post.setTargetSchool(req.getTargetSchool());
        post.setCategory(req.getCategory() != null ? req.getCategory() : "chat");
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(0);

        if (req.getTags() != null) {
            try {
                post.setTags(objectMapper.writeValueAsString(req.getTags()));
            } catch (Exception ignored) {}
        }

        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public void updatePost(Long id, PostRequest req, Long userId) {
        Post post = postMapper.selectById(id);
        if (post == null) throw new RuntimeException("帖子不存在");
        if (!post.getAuthorId().equals(userId)) throw new RuntimeException("无权限修改");

        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setTargetSchool(req.getTargetSchool());
        post.setCategory(req.getCategory());
        postMapper.updateById(post);
    }

    @Override
    public void deletePost(Long id, Long userId, String username) {
        Post post = postMapper.selectById(id);
        if (post == null) throw new RuntimeException("帖子不存在");
        // 管理员可以删除任何帖子，普通用户只能删除自己的帖子
        if (!"admin".equals(username) && !post.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限删除");
        }
        postMapper.deleteById(id);
    }

    @Override
    public boolean toggleLike(Long id, Long userId) {
        String likeKey = LIKE_KEY_PREFIX + id + ":" + userId;
        Boolean liked = redisTemplate.hasKey(likeKey);
        if (Boolean.TRUE.equals(liked)) {
            // 取消点赞
            redisTemplate.delete(likeKey);
            Post update = new Post();
            update.setId(id);
            Post current = postMapper.selectById(id);
            update.setLikeCount(Math.max(0, current.getLikeCount() - 1));
            postMapper.updateById(update);
            return false;
        } else {
            // 点赞
            redisTemplate.opsForValue().set(likeKey, "1", 30, TimeUnit.DAYS);
            Post update = new Post();
            update.setId(id);
            Post current = postMapper.selectById(id);
            update.setLikeCount(current.getLikeCount() + 1);
            postMapper.updateById(update);
            return true;
        }
    }

    @Override
    public Page<Map<String, Object>> getMyPosts(Long userId, int page, int size) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getAuthorId, userId)
                .orderByDesc(Post::getCreateTime);
        Page<Post> postPage = postMapper.selectPage(new Page<>(page, size), wrapper);
        Page<Map<String, Object>> resultPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        resultPage.setRecords(postPage.getRecords().stream().map(this::buildPostVO).toList());
        return resultPage;
    }

    private Map<String, Object> buildPostVO(Post post) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", post.getId());
        vo.put("title", post.getTitle());
        vo.put("content", post.getContent() != null && post.getContent().length() > 200
                ? post.getContent().substring(0, 200) + "..."
                : post.getContent());
        vo.put("authorId", post.getAuthorId());
        vo.put("targetSchool", post.getTargetSchool());
        vo.put("category", post.getCategory());
        vo.put("tags", post.getTags());
        vo.put("viewCount", post.getViewCount());
        vo.put("likeCount", post.getLikeCount());
        vo.put("commentCount", post.getCommentCount());
        vo.put("createTime", post.getCreateTime());

        // 查询作者信息
        User author = userMapper.selectById(post.getAuthorId());
        if (author != null) {
            vo.put("authorName", author.getNickname());
            vo.put("authorAvatar", author.getAvatar());
        }
        return vo;
    }
}
