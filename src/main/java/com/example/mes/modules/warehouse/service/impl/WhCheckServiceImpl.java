package com.example.mes.modules.warehouse.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhCheck;
import com.example.mes.modules.warehouse.mapper.WhCheckMapper;
import com.example.mes.modules.warehouse.service.WhCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhCheckServiceImpl implements WhCheckService {
    private final WhCheckMapper mapper;

    @Override @Transactional
    public WhCheck create(WhCheck entity) { mapper.insert(entity); return entity; }

    @Override @Transactional
    public WhCheck confirm(Long id) { mapper.confirm(id); return mapper.selectById(id); }

    @Override
    public PageResult<WhCheck> list(Long warehouseId, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<WhCheck> rows = mapper.selectList(warehouseId, status, offset, pageSize);
        long total = mapper.countList(warehouseId, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public WhCheck getById(Long id) { return mapper.selectById(id); }
}
