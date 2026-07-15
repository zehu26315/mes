package com.example.mes.modules.andon.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.andon.entity.AndonConfig;
import com.example.mes.modules.andon.mapper.AndonConfigMapper;
import com.example.mes.modules.andon.service.AndonConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AndonConfigServiceImpl implements AndonConfigService {

    private final AndonConfigMapper andonConfigMapper;

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.ANDON_CONFIG_LIST, allEntries = true)
    public AndonConfig create(AndonConfig andonConfig) {
        if (andonConfig.getStatus() == null) {
            andonConfig.setStatus(1);
        }
        if (andonConfig.getHandleType() == null) {
            andonConfig.setHandleType(1);
        }
        if (andonConfig.getNotifyMethod() == null) {
            andonConfig.setNotifyMethod(1);
        }
        andonConfigMapper.insert(andonConfig);
        return andonConfig;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.ANDON_CONFIG, CacheNames.ANDON_CONFIG_LIST}, allEntries = true)
    public AndonConfig update(AndonConfig andonConfig) {
        andonConfigMapper.update(andonConfig);
        return andonConfigMapper.selectById(andonConfig.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.ANDON_CONFIG, CacheNames.ANDON_CONFIG_LIST}, allEntries = true)
    public void delete(Long id) {
        andonConfigMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.ANDON_CONFIG, key = "#id", unless = "#result == null")
    public AndonConfig getById(Long id) {
        return andonConfigMapper.selectById(id);
    }

    @Override
    public PageResult<AndonConfig> list(String keyword, String andonType, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<AndonConfig> rows = andonConfigMapper.selectList(keyword, andonType, status, offset, pageSize);
        long total = andonConfigMapper.countList(keyword, andonType, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
