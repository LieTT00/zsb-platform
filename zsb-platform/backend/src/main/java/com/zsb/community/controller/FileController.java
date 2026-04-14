package com.zsb.community.controller;

import com.zsb.community.dto.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传接口
 * 支持图片上传（jpg, png, gif）
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_TYPES = {"jpg", "jpeg", "png", "gif"};

    /**
     * 上传图片
     * POST /api/file/upload
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("请选择要上传的文件");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            return Result.fail("文件大小不能超过 5MB");
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.fail("文件名无效");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean allowed = false;
        for (String type : ALLOWED_TYPES) {
            if (type.equals(extension)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            return Result.fail("仅支持上传 jpg、png、gif 格式的图片");
        }

        try {
            // 创建上传目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            File uploadDir = new File(uploadPath, datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String newFilename = UUID.randomUUID().toString() + "." + extension;
            File destFile = new File(uploadDir, newFilename);

            // 保存文件
            file.transferTo(destFile);

            // 返回访问路径
            String fileUrl = "/api/file/images/" + datePath + "/" + newFilename;
            return Result.success(fileUrl);
        } catch (IOException e) {
            return Result.fail("上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            return filename.substring(lastDot + 1);
        }
        return "";
    }
}
