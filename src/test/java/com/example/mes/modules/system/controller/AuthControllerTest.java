package com.example.mes.modules.system.controller;

import com.example.mes.common.exception.GlobalExceptionHandler;
import com.example.mes.common.response.LoginVO;
import com.example.mes.config.LoginInterceptor;
import com.example.mes.modules.system.service.SysLoginLogService;
import com.example.mes.modules.system.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController 认证接口")
class AuthControllerTest {

    @Mock
    private SysUserService sysUserService;
    @Mock
    private SysLoginLogService sysLoginLogService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(sysUserService, sysLoginLogService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/login 用户名密码正确 → 200 返回 LoginVO")
    void login_success() throws Exception {
        LoginVO loginVO = LoginVO.builder()
                .userId(1L)
                .username("admin")
                .nickname("管理员")
                .build();

        when(sysUserService.login("admin", "123456")).thenReturn(loginVO);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.nickname").value("管理员"));
    }

    @Test
    @DisplayName("POST /api/login 用户名为空 → 400")
    void login_emptyUsername() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户名和密码不能为空"));
    }

    @Test
    @DisplayName("POST /api/login 密码为空 → 400")
    void login_emptyPassword() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户名和密码不能为空"));
    }

    @Test
    @DisplayName("POST /api/login 凭据错误 → 400（经 GlobalExceptionHandler）")
    void login_badCredentials() throws Exception {
        when(sysUserService.login("admin", "wrong"))
                .thenThrow(new IllegalArgumentException("用户名或密码错误"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));
    }

    @Test
    @DisplayName("POST /api/logout → 200 登出成功")
    void logout() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("GET /api/auth/user-info 已登录 → 200 返回用户信息")
    void getUserInfo_authenticated() throws Exception {
        LoginVO loginVO = LoginVO.builder()
                .userId(1L)
                .username("admin")
                .nickname("管理员")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(LoginInterceptor.LOGIN_USER_KEY, loginVO);

        mockMvc.perform(get("/api/auth/user-info").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    @DisplayName("GET /api/auth/user-info 未登录 → 401")
    void getUserInfo_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/auth/user-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("未登录"));
    }
}
