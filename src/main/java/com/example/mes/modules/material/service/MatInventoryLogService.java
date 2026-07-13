package com.example.mes.modules.material.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatInventoryLog;

public interface MatInventoryLogService {

    PageResult<MatInventoryLog> list(Long materialId, String materialCode, String changeType, int page, int pageSize);
}
