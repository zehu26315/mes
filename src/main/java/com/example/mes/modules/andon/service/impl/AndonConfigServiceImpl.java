package com.example.mes.modules.andon.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonConfig;
import com.example.mes.modules.andon.mapper.AndonConfigMapper;
import com.example.mes.modules.andon.service.AndonConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AndonConfigServiceImpl implements AndonConfigService {

    private final AndonConfigMapper andonConfigMapper;

    @Override
    @Transactional
    public AndonConfig create(AndonConfig andonConfig) {
        if (andonConfig.getStatus() == null) {
            andonConfig.setStatus(1);
        }
        andonConfigMapper.insert(andonConfig);
        return andonConfig;
    }

    @Override
    @Transactional
    public AndonConfig update(AndonConfig andonConfig) {
        andonConfigMapper.update(andonConfig);
        return andonConfigMapper.selectById(andonConfig.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        andonConfigMapper.deleteById(id);
    }

    @Override
    public AndonConfig getById(Long id) {
        return andonConfigMapper.selectById(id);
    }

    @Override
    public PageResult<AndonConfig> list(String keyword, Long andonTypeId, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<AndonConfig> rows = andonConfigMapper.selectList(keyword, andonTypeId, status, offset, pageSize);
        long total = andonConfigMapper.countList(keyword, andonTypeId, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
