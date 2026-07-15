package com.example.mes.common.exception;

/**
 * 未登录或会话过期异常，应映射到 HTTP 401。
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
