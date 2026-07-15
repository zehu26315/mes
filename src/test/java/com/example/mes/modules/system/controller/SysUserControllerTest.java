package com.example.mes.modules.system.controller;

import com.example.mes.common.exception.GlobalExceptionHandler;
import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysUser;
import com.example.mes.modules.system.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysUserController 用户接口")
class SysUserControllerTest {

    @Mock
    private SysUserService sysUserService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new SysUserController(sysUserService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/sys/users → 200 创建用户")
    void create() throws Exception {
        SysUser created = SysUser.builder().id(1L).username("newuser").nickname("新用户").build();
        when(sysUserService.createUser(any(SysUser.class))).thenReturn(created);

        mockMvc.perform(post("/api/sys/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser\",\"password\":\"123456\",\"nickname\":\"新用户\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("newuser"));
    }

    @Test
    @DisplayName("PUT /api/sys/users/1 → 200 更新用户")
    void update() throws Exception {
        SysUser updated = SysUser.builder().id(1L).username("admin").nickname("新名称").build();
        when(sysUserService.updateUser(any(SysUser.class))).thenReturn(updated);

        mockMvc.perform(put("/api/sys/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"新名称\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.nickname").value("新名称"));
    }

    @Test
    @DisplayName("DELETE /api/sys/users/1 → 200 删除")
    void deleteUser() throws Exception {
        doNothing().when(sysUserService).deleteUser(1L);

        mockMvc.perform(delete("/api/sys/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("GET /api/sys/users/1 → 200 返回用户")
    void getById() throws Exception {
        SysUser user = SysUser.builder().id(1L).username("admin").nickname("管理员").build();
        when(sysUserService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/sys/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    @DisplayName("GET /api/sys/users 分页查询 → 200 返回分页数据")
    void list() throws Exception {
        SysUser user = SysUser.builder().id(1L).username("admin").build();
        PageResult<SysUser> pageResult = PageResult.of(1L, 1, 10, List.of(user));
        when(sysUserService.listUsers(eq(""), isNull(), eq(1), eq(10)))
                .thenReturn(pageResult);

        mockMvc.perform(get("/api/sys/users?page=1&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.rows.length()").value(1));
    }
}
