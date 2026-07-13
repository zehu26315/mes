package com.example.mes.modules.warehouse.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhOutbound;
import com.example.mes.modules.warehouse.mapper.WhOutboundMapper;
import com.example.mes.modules.warehouse.service.WhOutboundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhOutboundServiceImpl implements WhOutboundService {
    private final WhOutboundMapper mapper;

    @Override @Transactional
    public WhOutbound create(WhOutbound entity) { mapper.insert(entity); return entity; }

    @Override
    public PageResult<WhOutbound> list(String type, Long warehouseId, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<WhOutbound> rows = mapper.selectList(type, warehouseId, status, offset, pageSize);
        long total = mapper.countList(type, warehouseId, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public WhOutbound getById(Long id) { return mapper.selectById(id); }
}
