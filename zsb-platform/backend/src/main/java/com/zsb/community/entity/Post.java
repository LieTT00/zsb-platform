package com.zsb.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 帖子实体
 */
@Data
@TableName("t_post")
public class Post {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标题 */
    private String title;

    /** 正文内容 */
    private String content;

    /** 作者ID */
    private Long authorId;

    /** 目标院校 */
    private String targetSchool;

    /** 分类：experience/ask/resource/school/chat */
    private String category;

    /** 标签（JSON数组字符串） */
    private String tags;

    /** 浏览量 */
    private Integer viewCount;

    /** 点赞数 */
    private Integer likeCount;

    /** 评论数 */
    private Integer commentCount;

    /** 状态：0-正常 1-屏蔽 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标记 */
    private Integer deleted;
}
