package com.zsb.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（唯一） */
    private String username;

    /** 昵称 */
    private String nickname;

    /** MD5加密密码 */
    private String password;

    /** 头像URL */
    private String avatar;

    /** 目标院校 */
    private String targetSchool;

    /** 当前院校 */
    private String currentSchool;

    /** 个人简介 */
    private String bio;

    /** 状态：0-正常 1-禁用 */
    private Integer status;

    /** 最近登录时间 */
    private LocalDateTime lastLoginTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    /** 逻辑删除标记 */
    private Integer deleted;
}
