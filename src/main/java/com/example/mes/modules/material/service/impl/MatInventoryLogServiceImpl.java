package com.example.mes.modules.material.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatInventoryLog;
import com.example.mes.modules.material.mapper.MatInventoryLogMapper;
import com.example.mes.modules.material.service.MatInventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatInventoryLogServiceImpl implements MatInventoryLogService {

    private final MatInventoryLogMapper matInventoryLogMapper;

    @Override
    public PageResult<MatInventoryLog> list(Long materialId, String materialCode, String changeType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MatInventoryLog> rows = matInventoryLogMapper.selectList(materialId, materialCode, changeType, offset, pageSize);
        long total = matInventoryLogMapper.countList(materialId, materialCode, changeType);
        return PageResult.of(total, page, pageSize, rows);
    }
}
