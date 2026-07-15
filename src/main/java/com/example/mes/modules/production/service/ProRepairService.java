package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProRepair;

public interface ProRepairService {
    ProRepair create(ProRepair entity);
    ProRepair update(ProRepair entity);
    void delete(Long id);
    ProRepair getById(Long id);
    PageResult<ProRepair> list(String repairNo, Long workOrderId, Integer status, Integer repairType, int page, int pageSize);
}
