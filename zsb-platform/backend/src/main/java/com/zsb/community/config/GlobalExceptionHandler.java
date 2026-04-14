package com.zsb.community.config;

import com.zsb.community.dto.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 将后端异常转换为统一的 JSON 响应返回给前端
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（RuntimeException）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntime(RuntimeException e) {
        e.printStackTrace(); // 便于调试
        return Result.fail(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        return Result.fail(msg);
    }

    /**
     * 处理数据库异常（连接失败、SQL错误等）
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDatabase(DataAccessException e) {
        e.printStackTrace();
        String msg = e.getMessage();
        if (msg != null && msg.contains("Communications link failure")) {
            return Result.fail("数据库连接失败，请检查 MySQL 服务和配置");
        }
        if (msg != null && msg.contains("Unknown database")) {
            return Result.fail("数据库不存在，请先执行 init.sql 初始化脚本");
        }
        return Result.fail("数据库操作失败: " + msg);
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleGeneral(Exception e) {
        e.printStackTrace();
        return Result.fail("服务器内部错误: " + e.getMessage());
    }
}
