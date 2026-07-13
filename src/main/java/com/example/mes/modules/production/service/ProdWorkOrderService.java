package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdWorkOrder;

public interface ProdWorkOrderService {

    ProdWorkOrder create(ProdWorkOrder workOrder);

    ProdWorkOrder update(ProdWorkOrder workOrder);

    void delete(Long id);

    ProdWorkOrder getById(Long id);

    ProdWorkOrder getByOrderNo(String orderNo);

    PageResult<ProdWorkOrder> list(String keyword, Integer status, int page, int pageSize);

    void updateStatus(Long id, Integer status);
}
