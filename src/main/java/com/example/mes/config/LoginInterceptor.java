package com.example.mes.config;

import com.example.mes.common.response.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    /** Session attribute key for the logged-in user. */
    public static final String LOGIN_USER_KEY = "loginUser";

    private static final Set<String> WHITELIST = Set.of(
            "/api/login",
            "/api/logout"
    );

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 使用 servletPath 而非 requestURI：requestURI 含 context-path，
        // 配置了 server.servlet.context-path 时会导致白名单永不命中、登录被自锁
        String path = request.getServletPath();

        // 白名单放行
        if (WHITELIST.contains(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(LOGIN_USER_KEY) == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            Result<Void> result = Result.fail(401, "未登录或会话已过期");
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return false;
        }

        return true;
    }
}
