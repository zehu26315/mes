package com.example.mes.modules.system.service;

import com.example.mes.modules.system.entity.SysConfig;
import com.example.mes.modules.system.mapper.SysConfigMapper;
import com.example.mes.modules.system.service.impl.SysConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysConfigService 系统配置服务")
class SysConfigServiceImplTest {

    @Mock
    private SysConfigMapper sysConfigMapper;

    @InjectMocks
    private SysConfigServiceImpl service;

    private SysConfig config;

    @BeforeEach
    void setUp() {
        config = SysConfig.builder()
                .id(1L)
                .configName("站点名称")
                .configKey("site_name")
                .configValue("MES系统")
                .configType("string")
                .status(1)
                .isDeleted(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("更新配置 → configKey 置 null → 调用 mapper.update → 返回刷新实体")
    void updateConfig() {
        SysConfig input = SysConfig.builder()
                .id(1L)
                .configKey("should_be_nulled")
                .configValue("新值")
                .build();

        SysConfig refreshed = SysConfig.builder()
                .id(1L)
                .configKey(null)
                .configValue("新值")
                .build();

        when(sysConfigMapper.selectById(1L)).thenReturn(refreshed);

        SysConfig result = service.updateConfig(input);

        // configKey 被置 null
        assertNull(input.getConfigKey());
        // mapper.update 被调用
        verify(sysConfigMapper).update(input);
        // 返回刷新后的实体
        assertSame(refreshed, result);
    }

    @Test
    @DisplayName("删除配置 → 调用 mapper.deleteById")
    void deleteConfig() {
        service.deleteConfig(1L);
        verify(sysConfigMapper).deleteById(1L);
    }

    @Test
    @DisplayName("listAll → 返回 mapper 全部数据")
    void listAll() {
        when(sysConfigMapper.selectAll()).thenReturn(List.of(config));

        List<SysConfig> result = service.listAll();

        assertEquals(1, result.size());
        assertSame(config, result.get(0));
    }

    @Test
    @DisplayName("getConfigById 存在 → 返回配置")
    void getConfigById_found() {
        when(sysConfigMapper.selectById(1L)).thenReturn(config);

        SysConfig result = service.getConfigById(1L);

        assertSame(config, result);
    }

    @Test
    @DisplayName("getConfigById 不存在 → 返回 null")
    void getConfigById_notFound() {
        when(sysConfigMapper.selectById(999L)).thenReturn(null);

        SysConfig result = service.getConfigById(999L);

        assertNull(result);
    }
}
