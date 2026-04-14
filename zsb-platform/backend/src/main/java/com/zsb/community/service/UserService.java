package com.zsb.community.service;

import com.zsb.community.dto.RegisterRequest;
import com.zsb.community.entity.User;
import java.util.Map;

public interface UserService {
    void register(RegisterRequest req);
    User login(String username, String password);
    User getById(Long id);
    void updateInfo(Long userId, Map<String, String> updates);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
