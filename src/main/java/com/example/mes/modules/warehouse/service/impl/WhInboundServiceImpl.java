package com.example.mes.modules.warehouse.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhInbound;
import com.example.mes.modules.warehouse.mapper.WhInboundMapper;
import com.example.mes.modules.warehouse.service.WhInboundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhInboundServiceImpl implements WhInboundService {
    private final WhInboundMapper mapper;

    @Override @Transactional
    public WhInbound create(WhInbound entity) { mapper.insert(entity); return entity; }

    @Override
    public PageResult<WhInbound> list(String type, Long warehouseId, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<WhInbound> rows = mapper.selectList(type, warehouseId, status, offset, pageSize);
        long total = mapper.countList(type, warehouseId, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public WhInbound getById(Long id) { return mapper.selectById(id); }
}
