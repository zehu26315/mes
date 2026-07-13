package com.example.mes.modules.material.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatInventory;
import com.example.mes.modules.material.mapper.MatInventoryMapper;
import com.example.mes.modules.material.service.MatInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatInventoryServiceImpl implements MatInventoryService {

    private final MatInventoryMapper matInventoryMapper;

    @Override
    public PageResult<MatInventory> list(Long materialId, String materialCode, String warehouse, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MatInventory> rows = matInventoryMapper.selectList(materialId, materialCode, warehouse, offset, pageSize);
        long total = matInventoryMapper.countList(materialId, materialCode, warehouse);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public PageResult<MatInventory> getLowStock(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MatInventory> rows = matInventoryMapper.selectLowStock(offset, pageSize);
        long total = rows.size();
        return PageResult.of(total, page, pageSize, rows);
    }
}
