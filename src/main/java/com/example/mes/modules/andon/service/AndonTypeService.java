package com.example.mes.modules.andon.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonType;

public interface AndonTypeService {

    AndonType create(AndonType andonType);

    AndonType update(AndonType andonType);

    void delete(Long id);

    AndonType getById(Long id);

    PageResult<AndonType> list(String keyword, int page, int pageSize);
}
