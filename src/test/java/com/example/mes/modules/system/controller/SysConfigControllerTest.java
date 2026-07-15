package com.example.mes.modules.system.controller;

import com.example.mes.common.exception.GlobalExceptionHandler;
import com.example.mes.modules.system.entity.SysConfig;
import com.example.mes.modules.system.service.SysConfigService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysConfigController 系统配置接口")
class SysConfigControllerTest {

    @Mock
    private SysConfigService sysConfigService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new SysConfigController(sysConfigService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/sys/configs/1 → 200 返回配置")
    void getById() throws Exception {
        SysConfig config = SysConfig.builder()
                .id(1L).configName("站点名称").configKey("site_name").configValue("MES").build();
        when(sysConfigService.getConfigById(1L)).thenReturn(config);

        mockMvc.perform(get("/api/sys/configs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.configKey").value("site_name"));
    }

    @Test
    @DisplayName("GET /api/sys/configs → 200 返回全量列表")
    void listAll() throws Exception {
        SysConfig config = SysConfig.builder().id(1L).configName("站点名称").build();
        when(sysConfigService.listAll()).thenReturn(List.of(config));

        mockMvc.perform(get("/api/sys/configs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("PUT /api/sys/configs/1 → 200 更新并返回配置")
    void update() throws Exception {
        SysConfig updated = SysConfig.builder().id(1L).configName("新名称").configValue("新值").build();
        when(sysConfigService.updateConfig(any(SysConfig.class))).thenReturn(updated);

        mockMvc.perform(put("/api/sys/configs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"configName\":\"新名称\",\"configValue\":\"新值\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.configName").value("新名称"));
    }
}
