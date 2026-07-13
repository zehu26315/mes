package com.example.mes.modules.system.service.impl;

import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysConfig;
import com.example.mes.modules.system.mapper.SysConfigMapper;
import com.example.mes.modules.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_CONFIG, CacheNames.SYS_CONFIG_ALL}, allEntries = true)
    public SysConfig updateConfig(SysConfig config) {
        config.setConfigKey(null);
        sysConfigMapper.update(config);
        return sysConfigMapper.selectById(config.getId());
    }

    @Override
    @Cacheable(value = CacheNames.SYS_CONFIG_ALL, unless = "#result == null || #result.isEmpty()")
    public List<SysConfig> listAll() {
        return sysConfigMapper.selectAll();
    }

    @Override
    @Cacheable(value = CacheNames.SYS_CONFIG, key = "#id", unless = "#result == null")
    public SysConfig getConfigById(Long id) {
        return sysConfigMapper.selectById(id);
    }
}
