package com.example.mes.modules.andon.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonConfig;

public interface AndonConfigService {

    AndonConfig create(AndonConfig andonConfig);

    AndonConfig update(AndonConfig andonConfig);

    void delete(Long id);

    AndonConfig getById(Long id);

    PageResult<AndonConfig> list(String keyword, Long andonTypeId, Integer status, int page, int pageSize);
}
