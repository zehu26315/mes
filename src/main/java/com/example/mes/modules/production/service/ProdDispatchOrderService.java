package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdDispatchOrder;

public interface ProdDispatchOrderService {

    ProdDispatchOrder create(ProdDispatchOrder dispatchOrder);

    ProdDispatchOrder update(ProdDispatchOrder dispatchOrder);

    void delete(Long id);

    ProdDispatchOrder getById(Long id);

    PageResult<ProdDispatchOrder> list(Long workOrderId, Integer status, int page, int pageSize);
}
