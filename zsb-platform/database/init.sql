-- =============================================
-- 专升本一站式学习平台 — 数据库初始化脚本
-- 数据库：MySQL 8.x
-- 字符集：utf8mb4
-- =============================================

CREATE DATABASE IF NOT EXISTS zsb_community DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE zsb_community;

-- =============================================
-- 1. 用户表
-- =============================================
CREATE TABLE IF NOT EXISTS `t_user` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT         COMMENT '用户ID',
  `username`        VARCHAR(50)  NOT NULL                        COMMENT '用户名（唯一）',
  `nickname`        VARCHAR(50)  NOT NULL                        COMMENT '昵称',
  `password`        VARCHAR(64)  NOT NULL                        COMMENT 'MD5加密密码',
  `avatar`          VARCHAR(500) DEFAULT NULL                    COMMENT '头像URL',
  `target_school`   VARCHAR(100) DEFAULT NULL                    COMMENT '目标院校',
  `current_school`  VARCHAR(100) DEFAULT NULL                    COMMENT '当前院校',
  `bio`             VARCHAR(500) DEFAULT NULL                    COMMENT '个人简介',
  `status`          TINYINT      NOT NULL DEFAULT 0              COMMENT '状态：0-正常 1-禁用',
  `last_login_time` DATETIME     DEFAULT NULL                    COMMENT '最近登录时间',
  `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`         TINYINT      NOT NULL DEFAULT 0              COMMENT '逻辑删除：0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 2. 帖子表
-- =============================================
CREATE TABLE IF NOT EXISTS `t_post` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT         COMMENT '帖子ID',
  `title`           VARCHAR(200) NOT NULL                        COMMENT '标题',
  `content`         LONGTEXT     NOT NULL                        COMMENT '正文内容（支持Markdown）',
  `author_id`       BIGINT       NOT NULL                        COMMENT '作者用户ID',
  `target_school`   VARCHAR(100) DEFAULT NULL                    COMMENT '相关院校',
  `category`        VARCHAR(30)  NOT NULL DEFAULT 'chat'         COMMENT '分类：experience/ask/resource/school/chat',
  `tags`            VARCHAR(500) DEFAULT NULL                    COMMENT '标签（JSON数组）',
  `view_count`      INT          NOT NULL DEFAULT 0              COMMENT '浏览量',
  `like_count`      INT          NOT NULL DEFAULT 0              COMMENT '点赞数',
  `comment_count`   INT          NOT NULL DEFAULT 0              COMMENT '评论数',
  `status`          TINYINT      NOT NULL DEFAULT 0              COMMENT '状态：0-正常 1-屏蔽',
  `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`         TINYINT      NOT NULL DEFAULT 0              COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_target_school` (`target_school`),
  KEY `idx_category` (`category`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_like_count` (`like_count`),
  KEY `idx_view_count` (`view_count`),
  CONSTRAINT `fk_post_user` FOREIGN KEY (`author_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- =============================================
-- 3. 评论表
-- =============================================
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT           COMMENT '评论ID',
  `post_id`     BIGINT   NOT NULL                          COMMENT '帖子ID',
  `author_id`   BIGINT   NOT NULL                          COMMENT '评论者ID',
  `parent_id`   BIGINT   DEFAULT NULL                      COMMENT '父评论ID（NULL为顶级评论）',
  `content`     TEXT     NOT NULL                          COMMENT '评论内容',
  `like_count`  INT      NOT NULL DEFAULT 0                COMMENT '点赞数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted`     TINYINT  NOT NULL DEFAULT 0                COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_author_id` (`author_id`),
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `t_post` (`id`),
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`author_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- =============================================
-- 4. 点赞记录表（防止重复点赞）
-- =============================================
CREATE TABLE IF NOT EXISTS `t_post_like` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT           COMMENT 'ID',
  `post_id`     BIGINT   NOT NULL                          COMMENT '帖子ID',
  `user_id`     BIGINT   NOT NULL                          COMMENT '用户ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞记录表';

-- =============================================
-- 5. 公告表
-- =============================================
CREATE TABLE IF NOT EXISTS `t_announcement` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT         COMMENT '公告ID',
  `title`       VARCHAR(200) NOT NULL                        COMMENT '标题',
  `content`     LONGTEXT     NOT NULL                        COMMENT '内容',
  `type`        VARCHAR(20)  NOT NULL DEFAULT 'notice'        COMMENT '类型：notice-通知 announcement-公告 activity-活动',
  `status`      TINYINT      NOT NULL DEFAULT 1              COMMENT '状态：0-草稿 1-发布',
  `pinned`      TINYINT      NOT NULL DEFAULT 0              COMMENT '置顶：0-否 1-是',
  `view_count`  INT          NOT NULL DEFAULT 0              COMMENT '浏览量',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     TINYINT      NOT NULL DEFAULT 0              COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- =============================================
-- 6. 课程表
-- =============================================
CREATE TABLE IF NOT EXISTS `t_schedule` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT         COMMENT 'ID',
  `user_id`         BIGINT       NOT NULL                        COMMENT '用户ID',
  `date`            DATE         NOT NULL                        COMMENT '日期',
  `time_slot`       VARCHAR(20)  NOT NULL                        COMMENT '时间段：morning-上午 afternoon-下午 evening-晚上',
  `course_name`     VARCHAR(100) NOT NULL                        COMMENT '课程名称',
  `course_type`     VARCHAR(20)  NOT NULL DEFAULT 'study'        COMMENT '类型：study-学习 rest-休息 exam-考试',
  `remark`          VARCHAR(500) DEFAULT NULL                    COMMENT '备注',
  `attendance_status` VARCHAR(20) NOT NULL DEFAULT 'none'       COMMENT '打卡状态：none-未打卡 present-已签到 absent-缺席',
  `attendance_time` DATETIME     DEFAULT NULL                    COMMENT '打卡时间',
  `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`         TINYINT      NOT NULL DEFAULT 0              COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- =============================================
-- 7. 初始化测试数据
-- =============================================

-- 插入管理员用户（密码：123456 的MD5值）
INSERT INTO `t_user` (`username`, `nickname`, `password`, `target_school`, `status`)
VALUES
  ('admin', '管理员', 'e10adc3949ba59abbe56e057f20f883e', NULL, 0),
  ('test_user', '测试同学', 'e10adc3949ba59abbe56e057f20f883e', '武汉大学', 0),
  ('zhuang_ck', '庄同学', 'e10adc3949ba59abbe56e057f20f883e', '华中科技大学', 0);

-- 插入示例帖子
INSERT INTO `t_post` (`title`, `content`, `author_id`, `target_school`, `category`, `view_count`, `like_count`, `comment_count`)
VALUES
  ('【经验贴】从三本逆袭武汉大学的备考心得', '今年成功考上武汉大学计算机专业，分享一些备考心得...\n\n首先要端正态度，专升本其实并没有想象中那么难，关键是要找到适合自己的学习方法。', 1, '武汉大学', 'experience', 1520, 89, 34),
  ('华中科技大学软件工程专业报考指南', '整理了近三年华科软件工程的录取情况，2022年最低分432分...', 2, '华中科技大学', 'school', 2341, 147, 56),
  ('数学零基础如何备考？推荐这几本教材！', '很多同学数学底子薄，这里分享一套零基础的复习路线。', 3, NULL, 'resource', 3102, 213, 78);

-- 插入示例评论
INSERT INTO `t_comment` (`post_id`, `author_id`, `content`)
VALUES
  (1, 2, '太厉害了！请问英语是怎么备考的？'),
  (1, 3, '感谢分享，和我情况很像，加油！'),
  (2, 1, '信息很详细，感谢整理！');

-- 插入示例公告
INSERT INTO `t_announcement` (`title`, `content`, `type`, `status`, `pinned`, `view_count`)
VALUES
  ('🎓 欢迎使用专升本一站式学习平台', '亲爱的同学们，欢迎使用本平台！这里提供全面的专升本备考资源，包括经验分享、资料下载、视频课程等。祝愿大家都能考上理想的本科院校！\n\n平台功能：\n- 📝 帖子交流：分享学习经验、提问答疑\n- 📚 学习资料：免费下载备考资料\n- 🎬 视频课程：观看优质教学视频\n- 📊 数据统计：了解学习进度', 'announcement', 1, 1, 156),
  ('📢 关于2025年专升本考试时间的通知', '各省市2025年专升本考试时间陆续公布，请同学们密切关注所在省份教育考试院的通知。本平台将第一时间同步相关信息。\n\n建议同学们：\n1. 提前了解考试科目和大纲\n2. 制定合理的复习计划\n3. 多做真题，熟悉题型\n4. 保持良好心态，积极备考', 'notice', 1, 1, 89),
  ('🎯 平台新增学习视频功能', '为帮助同学们更好地备考，平台新增学习视频功能！\n\n目前已上线：\n- 高等数学基础课程\n- 大学英语语法讲解\n- 计算机基础系列\n\n更多优质课程持续更新中...', 'activity', 1, 0, 67);
