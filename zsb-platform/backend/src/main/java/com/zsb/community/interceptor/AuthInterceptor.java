package com.zsb.community.interceptor;

import com.zsb.community.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 验证请求头中的 Authorization Bearer Token
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        log.info("AuthInterceptor 初始化成功，JwtUtil 注入: {}", jwtUtil != null);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("AuthInterceptor.preHandle 被调用 - URI: {}", request.getRequestURI());
        
        // OPTIONS 请求直接放行（CORS 预检）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.debug("OPTIONS 请求放行");
            return true;
        }

        try {
            String token = extractToken(request);
            log.debug("提取到的 Token: {}", token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null");
            
            if (!StringUtils.hasText(token)) {
                log.warn("Token 为空");
                sendUnauthorized(response, "请先登录");
                return false;
            }
            
            if (!jwtUtil.validateToken(token)) {
                log.warn("Token 验证失败");
                sendUnauthorized(response, "登录已过期，请重新登录");
                return false;
            }

            // 将用户ID注入请求属性，供Controller使用
            Long userId = jwtUtil.getUserId(token);
            request.setAttribute("userId", userId);
            log.debug("Token 验证成功，用户ID: {}", userId);
            return true;
            
        } catch (Exception e) {
            log.error("Token 解析异常: {}", e.getMessage(), e);
            sendUnauthorized(response, "Token无效，请重新登录");
            return false;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\",\"data\":null}");
    }
}
