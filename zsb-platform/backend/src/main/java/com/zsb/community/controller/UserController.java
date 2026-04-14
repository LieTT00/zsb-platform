package com.zsb.community.controller;

import com.zsb.community.dto.*;
import com.zsb.community.entity.User;
import com.zsb.community.service.UserService;
import com.zsb.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户接口
 * 提供注册、登录、信息查询、修改等功能
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 用户注册
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest req) {
        userService.register(req);
        return Result.success("注册成功");
    }

    /**
     * 用户登录
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        User user = userService.login(req.getUsername(), req.getPassword());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", buildUserVO(user));
        return Result.success(data);
    }

    /**
     * 获取当前用户信息
     * GET /api/user/info
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestAttribute("userId") Long userId) {
        User user = userService.getById(userId);
        if (user == null) return Result.fail("用户不存在");
        return Result.success(buildUserVO(user));
    }

    /**
     * 更新用户信息
     * PUT /api/user/info
     */
    @PutMapping("/info")
    public Result<?> updateInfo(
            @RequestAttribute("userId") Long userId,
            @RequestBody Map<String, String> updates
    ) {
        userService.updateInfo(userId, updates);
        return Result.success("修改成功");
    }

    /**
     * 修改密码
     * PUT /api/user/password
     */
    @PutMapping("/password")
    public Result<?> changePassword(
            @RequestAttribute("userId") Long userId,
            @RequestBody Map<String, String> body
    ) {
        userService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
        return Result.success("密码修改成功");
    }

    private Map<String, Object> buildUserVO(User user) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", user.getId());
        vo.put("username", user.getUsername());
        vo.put("nickname", user.getNickname());
        vo.put("avatar", user.getAvatar());
        vo.put("school", user.getTargetSchool());
        vo.put("bio", user.getBio());
        vo.put("joinDate", user.getCreateTime() != null ? user.getCreateTime().toLocalDate().toString() : "");
        return vo;
    }
}
