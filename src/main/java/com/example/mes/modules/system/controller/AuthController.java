package com.example.mes.modules.system.controller;

import com.example.mes.common.exception.UnauthorizedException;
import com.example.mes.common.response.LoginVO;
import com.example.mes.common.response.Result;
import com.example.mes.config.LoginInterceptor;
import com.example.mes.modules.system.entity.SysLoginLog;
import com.example.mes.modules.system.service.SysLoginLogService;
import com.example.mes.modules.system.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final SysUserService sysUserService;
    private final SysLoginLogService sysLoginLogService;

    @PostMapping("/api/login")
    public Result<LoginVO> login(@RequestBody Map<String, String> body,
                                  HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        LoginVO loginVO = null;
        String errorMsg = null;
        try {
            loginVO = sysUserService.login(username, password);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            recordLoginLog(request, username, 1, errorMsg);
            throw e;
        }

        // 防止 Session 固定：销毁旧 session，创建新 session
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(LoginInterceptor.LOGIN_USER_KEY, loginVO);

        recordLoginLog(request, username, 0, "登录成功");
        return Result.ok(loginVO);
    }

    private void recordLoginLog(HttpServletRequest request, String username, int status, String msg) {
        try {
            String userAgent = request.getHeader("User-Agent");
            SysLoginLog loginLog = SysLoginLog.builder()
                    .loginName(username)
                    .ipAddress(request.getRemoteAddr())
                    .browserType(userAgent)
                    .status(status)
                    .msg(msg)
                    .build();
            sysLoginLogService.record(loginLog);
        } catch (Exception e) {
            log.warn("Failed to record login log: {}", e.getMessage());
        }
    }

    @PostMapping("/api/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
    }

    @GetMapping("/api/auth/user-info")
    public Result<LoginVO> userInfo(HttpSession session) {
        Object attr = session.getAttribute(LoginInterceptor.LOGIN_USER_KEY);
        if (!(attr instanceof LoginVO loginVO)) {
            throw new UnauthorizedException("未登录");
        }
        return Result.ok(loginVO);
    }
}
