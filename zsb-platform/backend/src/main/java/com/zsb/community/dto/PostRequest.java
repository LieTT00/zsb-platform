package com.zsb.community.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 发帖请求
 */
@Data
public class PostRequest {

    @NotBlank(message = "标题不能为空")
    @Size(min = 5, max = 100, message = "标题 5-100 字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(min = 20, max = 10000, message = "内容 20-10000 字符")
    private String content;

    /** 目标院校 */
    private String targetSchool;

    /** 分类 */
    private String category;

    /** 标签列表 */
    private List<String> tags;
}
