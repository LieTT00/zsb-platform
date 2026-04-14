package com.zsb.community.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户注册请求
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度 3-20 位")
    private String username;

    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 15, message = "昵称 2-15 字符")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码至少 6 位")
    private String password;

    /** 目标院校（可选） */
    private String targetSchool;
}
