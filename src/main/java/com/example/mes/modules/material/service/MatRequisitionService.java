package com.example.mes.modules.material.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatRequisition;

public interface MatRequisitionService {

    MatRequisition create(MatRequisition requisition);

    MatRequisition getById(Long id);

    PageResult<MatRequisition> list(String status, Long workOrderId, String materialCode, int page, int pageSize);

    void approve(Long id);
}
