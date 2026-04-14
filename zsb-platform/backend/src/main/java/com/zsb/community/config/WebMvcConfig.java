package com.zsb.community.config;

import com.zsb.community.interceptor.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 注册认证拦截器、跨域（CORS）设置、静态资源
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Autowired
    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
        log.info("WebMvcConfig 初始化，AuthInterceptor: {}", authInterceptor);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册 AuthInterceptor 到 /api/** 路径");
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/post/list",
                        "/api/post/{id}",
                        "/api/stats/overview",
                        "/api/stats/monthly",
                        "/api/stats/post-trend",
                        "/api/announcement/**",
                        "/api/file/upload",
                        "/api/file/images/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置图片访问路径
        registry.addResourceHandler("/api/file/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
