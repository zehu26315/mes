package com.example.mes.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Result 统一响应")
class ResultTest {

    @Test
    @DisplayName("ok(T data) → code=200, message=success, data 正确")
    void ok_withData() {
        String data = "hello";
        Result<String> result = Result.ok(data);

        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("hello", result.getData());
    }

    @Test
    @DisplayName("ok() → code=200, message=success, data=null")
    void ok_noData() {
        Result<Void> result = Result.ok();

        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("fail(int code, String msg) → 自定义 code 和 message")
    void fail_withCodeAndMessage() {
        Result<Void> result = Result.fail(400, "参数错误");

        assertEquals(400, result.getCode());
        assertEquals("参数错误", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("fail(String msg) → code=500")
    void fail_messageOnly() {
        Result<Void> result = Result.fail("服务器内部错误");

        assertEquals(500, result.getCode());
        assertEquals("服务器内部错误", result.getMessage());
        assertNull(result.getData());
    }
}
