package com.example.mes.modules.trace.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.trace.entity.TraceMaterialBatch;
import com.example.mes.modules.trace.mapper.TraceMaterialBatchMapper;
import com.example.mes.modules.trace.service.TraceMaterialBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraceMaterialBatchServiceImpl implements TraceMaterialBatchService {

    private final TraceMaterialBatchMapper traceMaterialBatchMapper;

    @Override
    @Transactional
    public TraceMaterialBatch createMaterialBatch(TraceMaterialBatch materialBatch) {
        traceMaterialBatchMapper.insert(materialBatch);
        return materialBatch;
    }

    @Override
    public TraceMaterialBatch getMaterialBatchById(Long id) {
        return traceMaterialBatchMapper.selectById(id);
    }

    @Override
    public TraceMaterialBatch getMaterialBatchByBatchNo(String batchNo) {
        return traceMaterialBatchMapper.selectByBatchNo(batchNo);
    }

    @Override
    public PageResult<TraceMaterialBatch> listMaterialBatches(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<TraceMaterialBatch> rows = traceMaterialBatchMapper.selectList(offset, pageSize);
        long total = traceMaterialBatchMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }
}
