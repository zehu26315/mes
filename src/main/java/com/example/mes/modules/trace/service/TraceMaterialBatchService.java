package com.example.mes.modules.trace.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.trace.entity.TraceMaterialBatch;

public interface TraceMaterialBatchService {

    TraceMaterialBatch createMaterialBatch(TraceMaterialBatch materialBatch);

    TraceMaterialBatch getMaterialBatchById(Long id);

    TraceMaterialBatch getMaterialBatchByBatchNo(String batchNo);

    PageResult<TraceMaterialBatch> listMaterialBatches(int page, int pageSize);
}
