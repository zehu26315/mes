package com.example.mes.modules.material.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatSupplement;

public interface MatSupplementService {

    MatSupplement create(MatSupplement supplement);

    MatSupplement getById(Long id);

    PageResult<MatSupplement> list(String status, Long workOrderId, String materialCode, int page, int pageSize);

    void approve(Long id);
}
