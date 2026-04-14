package com.zsb.community.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zsb.community.dto.RegisterRequest;
import com.zsb.community.entity.User;
import com.zsb.community.mapper.UserMapper;
import com.zsb.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void register(RegisterRequest req) {
        // 校验用户名唯一
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername())
        );
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        // 构造用户
        User user = new User();
        user.setUsername(req.getUsername());
        user.setNickname(req.getNickname());
        user.setPassword(DigestUtil.md5Hex(req.getPassword())); // MD5加密
        user.setTargetSchool(req.getTargetSchool());
        user.setStatus(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public User login(String username, String password) {
        String md5pwd = DigestUtil.md5Hex(password);
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getPassword, md5pwd)
        );
        if (user == null) throw new RuntimeException("用户名或密码错误");
        if (user.getStatus() == 1) throw new RuntimeException("账号已被禁用");
        return user;
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void updateInfo(Long userId, Map<String, String> updates) {
        User user = new User();
        user.setId(userId);
        if (StringUtils.hasText(updates.get("nickname"))) user.setNickname(updates.get("nickname"));
        if (updates.containsKey("bio")) user.setBio(updates.get("bio"));
        if (updates.containsKey("targetSchool")) user.setTargetSchool(updates.get("targetSchool"));
        if (updates.containsKey("currentSchool")) user.setCurrentSchool(updates.get("currentSchool"));
        userMapper.updateById(user);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        if (!DigestUtil.md5Hex(oldPassword).equals(user.getPassword())) {
            throw new RuntimeException("当前密码错误");
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(DigestUtil.md5Hex(newPassword));
        userMapper.updateById(update);
    }
}
