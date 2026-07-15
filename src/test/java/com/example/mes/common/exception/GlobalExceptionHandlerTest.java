package com.example.mes.common.exception;

import com.example.mes.common.response.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler 全局异常处理")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("IllegalArgumentException → 400 + 异常消息")
    void handleIllegalArgument() {
        Result<Void> result = handler.handleIllegalArgument(
                new IllegalArgumentException("用户名不能为空"));

        assertEquals(400, result.getCode());
        assertEquals("用户名不能为空", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("UnauthorizedException → 401 + 异常消息")
    void handleUnauthorized() {
        Result<Void> result = handler.handleUnauthorized(
                new UnauthorizedException("未登录"));

        assertEquals(401, result.getCode());
        assertEquals("未登录", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("DataIntegrityViolationException 含 Duplicate entry → 400 + 重复值提示")
    void handleDataIntegrity_duplicateEntry() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
                "Duplicate entry 'admin' for key 'uk_username'");

        Result<Void> result = handler.handleDataIntegrity(ex);

        assertEquals(400, result.getCode());
        assertTrue(result.getMessage().contains("admin"));
        assertTrue(result.getMessage().contains("已存在"));
        assertNull(result.getData());
    }

    @Test
    @DisplayName("DataIntegrityViolationException 不含 Duplicate entry → 通用冲突提示")
    void handleDataIntegrity_noDuplicate() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
                "Cannot delete or update a parent row");

        Result<Void> result = handler.handleDataIntegrity(ex);

        assertEquals(400, result.getCode());
        assertEquals("数据冲突，请检查后重试", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("DataIntegrityViolationException.message=null → 通用冲突提示")
    void handleDataIntegrity_nullMessage() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(null,
                new SQLIntegrityConstraintViolationException());

        Result<Void> result = handler.handleDataIntegrity(ex);

        assertEquals(400, result.getCode());
        assertEquals("数据冲突，请检查后重试", result.getMessage());
    }

    @Test
    @DisplayName("通用 Exception → 500 + 系统内部错误")
    void handleGenericException() {
        Result<Void> result = handler.handleException(new RuntimeException("boom"), null);

        assertEquals(500, result.getCode());
        assertEquals("系统内部错误，请联系管理员", result.getMessage());
        assertNull(result.getData());
    }
}
