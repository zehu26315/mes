package com.example.mes.modules.warehouse.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhTransfer;
import com.example.mes.modules.warehouse.mapper.WhTransferMapper;
import com.example.mes.modules.warehouse.service.WhTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhTransferServiceImpl implements WhTransferService {
    private final WhTransferMapper mapper;

    @Override @Transactional
    public WhTransfer create(WhTransfer entity) { mapper.insert(entity); return entity; }

    @Override
    public PageResult<WhTransfer> list(String status, Long fromWarehouseId, Long toWarehouseId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<WhTransfer> rows = mapper.selectList(status, fromWarehouseId, toWarehouseId, offset, pageSize);
        long total = mapper.countList(status, fromWarehouseId, toWarehouseId);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public WhTransfer getById(Long id) { return mapper.selectById(id); }
}
