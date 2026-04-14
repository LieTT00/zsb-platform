package com.zsb.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告实体
 */
@Data
@TableName("t_announcement")
public class Announcement {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 类型：notice-通知 announcement-公告 activity-活动 */
    private String type;

    /** 状态：0-草稿 1-发布 */
    private Integer status;

    /** 置顶：0-否 1-是 */
    private Integer pinned;

    /** 浏览量 */
    private Integer viewCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    private Integer deleted;
}
