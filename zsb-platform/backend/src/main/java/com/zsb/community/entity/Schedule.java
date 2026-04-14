package com.zsb.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课程表实体
 */
@Data
@TableName("t_schedule")
public class Schedule {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 日期 */
    private LocalDate date;

    /** 时间段：morning-上午 afternoon-下午 evening-晚上 */
    private String timeSlot;

    /** 课程名称 */
    private String courseName;

    /** 课程类型：study-学习 rest-休息 exam-考试 */
    private String courseType;

    /** 备注 */
    private String remark;

    /** 打卡状态：none-未打卡 present-已签到 absent-缺席 */
    private String attendanceStatus;

    /** 打卡时间 */
    private LocalDateTime attendanceTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    private Integer deleted;
}
